/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.batik.ext.awt.image.codec.jpeg;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.DeferRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.ext.awt.image.rendered.Any2sRGBRed;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;
import org.apache.batik.util.ParsedURL;
import org.imgscalr.Scalr;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import com.praveen.renderer.RenderingContants;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.TruncatedFileException;



/**
 *
 * @version $Id: JPEGRegistryEntry.java 501094 2007-01-29 16:35:37Z deweese $
 */
public class JPEGRegistryEntry
    extends MagicNumberRegistryEntry {

    static final byte [] sigJPEG   = {(byte)0xFF, (byte)0xd8,
                                      (byte)0xFF};
    static final String [] exts      = {"jpeg", "jpg" };
    static final String [] mimeTypes = {"image/jpeg", "image/jpg" };
    static final MagicNumber [] magicNumbers = {
        new MagicNumber(0, sigJPEG)
    };

    public JPEGRegistryEntry() {
        super("JPEG", exts, mimeTypes, magicNumbers);
    }

    /**
     * Decode the Stream into a RenderableImage
     *
     * @param inIS The input stream that contains the image.
     * @param origURL The original URL, if any, for documentation
     *                purposes only.  This may be null.
     * @param needRawData If true the image returned should not have
     *                    any default color correction the file may
     *                    specify applied.
     */
    public Filter handleStream(InputStream inIS,
                               ParsedURL   origURL,
                               boolean     needRawData) {
        final DeferRable  dr  = new DeferRable();
        final InputStream is  = inIS;
        final String      errCode;
        final Object []   errParam;
        if (origURL != null) {
            errCode  = ERR_URL_FORMAT_UNREADABLE;
            errParam = new Object[] {"JPEG", origURL};
        } else {
            errCode  = ERR_STREAM_FORMAT_UNREADABLE;
            errParam = new Object[] {"JPEG"};
        }

        Thread t = new Thread() {
                public void run() {
                    Filter filt;
                    try{
                        JPEGImageDecoder decoder;
                        decoder = JPEGCodec.createJPEGDecoder(is);
                        BufferedImage image;
                        try {
                            image   = decoder.decodeAsBufferedImage();
                        } catch (TruncatedFileException tfe) {
                            image = tfe.getBufferedImage();
                            // Should probably draw some indication
                            // that this is a partial image....
                            if (image == null)
                                throw new IOException
                                    ("JPEG File was truncated");
                        }
                        // Praveen Implementation of interpolation
                        if(RenderingContants.DEFAULT_INTERPOLATION != getIntepolationType()){
                        	image = getScaledImage(image);
                        }
                        
                        dr.setBounds(new Rectangle2D.Double
                                     (0, 0, image.getWidth(),
                                      image.getHeight()));
                        CachableRed cr;
                        cr = GraphicsUtil.wrap(image);
                        cr = new Any2sRGBRed(cr);
                        cr = new FormatRed(cr, GraphicsUtil.sRGB_Unpre);
                        WritableRaster wr = (WritableRaster)cr.getData();
                        ColorModel cm = cr.getColorModel();
                        image = new BufferedImage
                            (cm, wr, cm.isAlphaPremultiplied(), null);
                        cr = GraphicsUtil.wrap(image);
                        filt = new RedRable(cr);
                    } catch (IOException ioe) {
                        // Something bad happened here...
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (JPEGRegistryEntry.this, errCode, errParam);
                    } catch (ThreadDeath td) {
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (JPEGRegistryEntry.this, errCode, errParam);
                        dr.setSource(filt);
                        throw td;
                    } catch (Throwable t) {
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (JPEGRegistryEntry.this, errCode, errParam);
                    }

                    dr.setSource(filt);
                }

            };
        t.start();
        return dr;
    }
    // praveen Implementation
    private int intepolationType;
    private long toHeight;
    private long toWidth;
    
    

	public int getIntepolationType() {
		return intepolationType;
	}

	public void setIntepolationType(int intepolationType) {
		System.out.println("intepolationType : " + intepolationType);
		this.intepolationType = intepolationType;
	}

	public int getToHeight() {
		return (int) toHeight;
	}

	public int getToWidth() {
		return (int) toWidth;
	}

	@Override
	public void scaleTo(long toHeight, long toWidth, int intepolationType) {
		// TODO Auto-generated method stub
		this.toHeight = toHeight;
		this.toWidth = toWidth;
		this.intepolationType = intepolationType;
	}
	public static final int Lanczos = 0;
	
	public BufferedImage getScaledImage(BufferedImage image){
		
		if(RenderingContants.IMAGE_SCALAR_INTERPOLATION == getIntepolationType()){
			getImgSclr(image);
		} else {
			System.out.println("Custom interpolation" + getIntepolationType());
			mortenNobel(image);
		}
		return image;
	}
	
	public BufferedImage getMortenNobel(BufferedImage image) {
		BufferedImage resizedImage;
		ResampleOp resampleOp = new ResampleOp(getToWidth(), getToHeight());
		if(RenderingContants.BELLFILTER_INTERPOLATION == getIntepolationType()){
			resampleOp.setFilter(ResampleFilters.getBellFilter());
		} else if (RenderingContants.BICUBIC_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getBiCubicFilter());
		} else if (RenderingContants.BOX_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getBoxFilter());
		} else if (RenderingContants.BSPLINE_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getBSplineFilter());
		} else if (RenderingContants.HERMITE_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getHermiteFilter());
		} else if (RenderingContants.MITCHELL_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getMitchellFilter());
		} else if (RenderingContants.LANCZOS_INTERPOLATION == getIntepolationType()) {
			resampleOp.setFilter(ResampleFilters.getLanczos3Filter());
		}
		resizedImage = new BufferedImage(getToWidth(), getToHeight(), image.getType());
		resizedImage = resampleOp.doFilter(image, resizedImage, getToWidth(), getToHeight());
		//image = resizedImage;
		return resizedImage;
	}
	
	public BufferedImage getImgSclr(BufferedImage origImage){
		BufferedImage resizedImage = Scalr.resize(origImage,
                Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, getToWidth(),
                getToHeight(), Scalr.OP_ANTIALIAS);
		origImage = resizedImage;
		return origImage;
	}
	
	public BufferedImage mortenNobel(BufferedImage image) {
		System.out.println("Before Height : " + image.getHeight() + image.getWidth());
		BufferedImage resizedImage;
		ResampleOp resampleOp = new ResampleOp(getToWidth(), getToHeight());
		ResampleFilters filters = new ResampleFilters();
		resampleOp.setFilter(filters.getLanczos3Filter());
		resizedImage = new BufferedImage(getToWidth(), getToHeight(), image.getType());
		resizedImage = resampleOp.doFilter(image, resizedImage, getToWidth(), getToHeight());
		image = resizedImage;
		System.out.println("After Height : " + image.getHeight() + image.getWidth());
		return image;
	}
	
}
