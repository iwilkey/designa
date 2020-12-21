package dev.iwilkey.designa.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
	public static String readFile(InputStream in) throws IOException {
		final StringBuffer sBuffer = new StringBuffer();
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		final char[] buffer = new char[1024];

		int cnt;
		while ((cnt = br.read(buffer, 0, buffer.length)) > -1) {
			sBuffer.append(buffer, 0, cnt);
		}
		br.close();
		in.close();
		return sBuffer.toString();
	}
}
