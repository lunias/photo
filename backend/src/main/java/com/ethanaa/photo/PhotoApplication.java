package com.ethanaa.photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;

@SpringBootApplication
public class PhotoApplication {

	static  {
		// Load deferred color space profiles to avoid ConcurrentModificationException due to JDK
		// Use in public static main void or prior to application initialization
		// https://github.com/haraldk/TwelveMonkeys/issues/402
		// https://bugs.openjdk.java.net/browse/JDK-6986863
		// https://stackoverflow.com/questions/26297491/imageio-thread-safety
		ICC_Profile.getInstance(ColorSpace.CS_sRGB).getData();
		ICC_Profile.getInstance(ColorSpace.CS_PYCC).getData();
		ICC_Profile.getInstance(ColorSpace.CS_GRAY).getData();
		ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ).getData();
		ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB).getData();
	}

	public static void main(String[] args) {

		SpringApplication.run(PhotoApplication.class, args);
	}
}
