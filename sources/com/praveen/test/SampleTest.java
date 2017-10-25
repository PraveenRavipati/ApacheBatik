package com.praveen.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import com.praveen.renderer.RenderingContants;


public class SampleTest {
	public static void main(String[] args) {
		String fileNames = "/home/praveen/Desktop/Rendering.svg";
		
		//String fileNames = "/home/praveen/Desktop/SVG_Files/Sanjith.svg";
		//for (int i = 0; i < fileNames.length; i++) {
			convertion(fileNames,"/home/praveen/Desktop/Image1");
			
		//}
	}
	
	public static void convertion(String fileName,String outputName){
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			TranscoderInput input = new TranscoderInput(fileInputStream);
			Transcoder transcoder = getTranscoder();
			//transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT , 1000);
			//transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH , 1000);
	        OutputStream ostream = new FileOutputStream(outputName + "_normal.jpg");
	        TranscoderOutput output_document = new TranscoderOutput(ostream);
	        transcoder.transcode(input, output_document);
	        fileInputStream.close();
			ostream.close();
			System.out.println("done");
			//transcoder.transcode(input, output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static Transcoder getTranscoder(){
		Transcoder transcoder = new JPEGTranscoder(){
			@Override
			protected ImageRenderer createRenderer() {
				ImageRenderer renderer = super.createRenderer();
				java.awt.RenderingHints hints = renderer.getRenderingHints();
				//hints.add(new java.awt.RenderingHints(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
				//hints.add(new java.awt.RenderingHints(java.awt.RenderingHints.KEY_INTERPOLATION,java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR));
				//hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
				renderer.setRenderingHints(hints);
				//this.setIntepolationType(RenderingContants.BICUBIC_INTERPOLATION);
				return renderer;
			}
		};
		if(transcoder instanceof JPEGTranscoder){
			JPEGTranscoder jpegTranscoder = (JPEGTranscoder) transcoder;
			//jpegTranscoder.setIntepolationType(RenderingContants.LANCZOS_INTERPOLATION);
			transcoder = jpegTranscoder;
		}
		transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.99f);
		return transcoder;
	}
	
	public void InterpolationTest() {
		System.out.println("nothing");
	}
}
//For image SVGImageElementBridge