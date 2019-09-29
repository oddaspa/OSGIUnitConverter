package no.tdt4250.assignment2.conversionApp.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class DictServletTest {

	@Test
	public void testDict() throws IOException {
		String httpPort = System.getProperty("org.osgi.service.http.port", "8080");
		URL url = new URL("http://localhost:" + httpPort + "/dict?q=hei");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(200, con.getResponseCode());
	}
}

