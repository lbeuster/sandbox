package de.lbe.sandbox.ftp;

import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;
import de.asideas.ipool.shared.importer.type.ftp.test.EmbeddedFtpServer;

/**
 * @author lbeuster
 */
public class FtpTest extends AbstractJUnitTest {

	private static final boolean EMBEDDED = true;
	private static final Integer PORT = 2222;
	private static final String HOST = "localhost:" + PORT;
	private static final String USER = "test_ftp";
	private static final String PASS = "test_ftp_pass";

	// private static final boolean EMBEDDED = false;
	// private static final String HOST = "ipooldev.exavault.com";
	// private static final String USER = "larstest";
	// private static final String PASS = "larstest_passwd";
	// private static final Integer PORT = null;

	private EmbeddedFtpServer ftpServer;

	// @Before
	// public void setUp() throws Exception {
	// PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	// UserManager userManager = userManagerFactory.createUserManager();
	// BaseUser user = new BaseUser();
	// user.setName("test_ftp");
	// user.setPassword("test_ftp_pass");
	// user.setHomeDirectory("/tmp");
	// userManager.save(user);
	//
	// ListenerFactory listenerFactory = new ListenerFactory();
	// listenerFactory.setPort(2222);
	//
	// FtpServerFactory factory = new FtpServerFactory();
	// factory.setUserManager(userManager);
	// factory.addListener("default", listenerFactory.createListener());
	//
	// FtpServer server = factory.createServer();
	// server.start();
	// System.out.println("ftpserver started");
	// }
	//
	@Before
	public void setUp() throws Exception {
		if (EMBEDDED) {
			ftpServer = new EmbeddedFtpServer();
			ftpServer.addUser(USER, PASS);
			ftpServer.setPort(PORT);
			ftpServer.start();
		}
	}

	@After
	public void tearDown() throws Exception {
		if (this.ftpServer != null) {
			this.ftpServer.stop();
		}
	}

	@Test
	public void testUsage() throws Exception {
		FTPClientExample.main();
	}

	@Test
	public void testListSecure() throws Exception {
		FTPClientExample.main("-l", "-p", "false", HOST, USER, PASS);
	}

	@Test
	public void testList() throws Exception {
		// FTPClientExample.main("-l", "-p", "false", HOST, USER, PASS);
		FTPClientExample.main("-l", HOST, USER, PASS);
	}

	@Test
	public void testUpload() throws Exception {
		FTPClientExample.main("-s", HOST, USER, PASS, "small.txt", "small.txt");
		FTPClientExample.main("-l", HOST, USER, PASS, "*");
	}

	@Test
	public void testDownload() throws Exception {
		FTPClientExample.main(HOST, USER, PASS, "small.txt_0", "downloaded.txt");
	}

	/**
	 *
	 */
	public void testAll() throws Exception {
		FTPClient ftp = new FTPClient();
		try {
			ftp.setListHiddenFiles(true);
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
			ftp.setConnectTimeout(5_000);
			ftp.connect(HOST);
			ftp.setSoTimeout(5_000);
			int reply = ftp.getReplyCode();
			if (!ftp.login(USER, PASS)) {
				throw new RuntimeException("Could not login");
			}

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				return;
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			ftp.logout();
		} finally {
			ftp.disconnect();
		}
	}
}