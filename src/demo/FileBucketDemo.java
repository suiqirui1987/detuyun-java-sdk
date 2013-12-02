package demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.DetuYun;

/**
 * 文件类空间的demo
 */
public class FileBucketDemo {

	// 运行前先设置好以下三个参数
	private static final String BUCKET_NAME = "abcdd";//"文件类空间名";
	private static final String USER_NAME = "faith196";//"操作员名";
	private static final String USER_PWD = "fhx442gh1n1qmeuqyvmtf5nt2uk482";//"操作员密码";

	/** 根目录 */
	private static final String DIR_ROOT = "/";
	/** 多级目录 */
	private static final String DIR_MORE = "/1/2/3/";
	/** 目录名 */
	private static final String FOLDER_NAME = "tmp";
	/** 上传到detuyun的文件名 */
	private static final String FILE_NAME = "college.jpg";

	/** 本地待上传的测试文件 */
	private static final String SAMPLE_TXT_FILE = System
			.getProperty("user.dir") + "/" + FILE_NAME;

	private static DetuYun detuyun = null;

	static {
		File txtFile = new File(SAMPLE_TXT_FILE);

		if (!txtFile.isFile()) {
			System.out.println("本地待上传的测试文件不存在！");
		}
	}

	public static void main(String[] args) throws Exception {

		// 初始化空间
		detuyun = new DetuYun(BUCKET_NAME, USER_NAME, USER_PWD);

		// ****** 可选设置 begin ******

		// 设置连接超时时间，默认为30秒
		// detuyun.setTimeout(60);

		// 设置是否开启debug模式，默认不开启
		detuyun.setDebug(true);

		// ****** 可选设置 end ******

		// 1.创建目录，有两种形式
		testMkDir();

		// 2.上传文件，图片空间的文件上传请参考 PicBucketDemo.java
		testWriteFile();

		// 3.获取文件信息
		testGetFileInfo();

		// 4.读取目录
		testReadDir();

		// 5.获取空间占用大小
		testGetBucketUsage();

		// 7.读取文件/下载文件
		testReadFile();

		// 8.删除文件
		testDeleteFile();

		// 9.删除目录
		testRmDir();
	}

	/**
	 * 获取空间占用大小
	 */
	public static void testGetBucketUsage() {

		long usage = detuyun.getBucketUsage();

		System.out.println("空间总使用量：" + usage + "kb");
		System.out.println();
	}

	/**
	 * 上传文件
	 * 
	 * @throws IOException
	 */
	public static void testWriteFile() throws IOException {

		// 要传到detuyun后的文件路径
		String filePath = DIR_ROOT + FILE_NAME;


		/*
		 * 采用数据流模式上传文件（节省内存），可自动创建父级目录（最多10级）
		 */
		File file = new File(SAMPLE_TXT_FILE);
		boolean result3 = detuyun.writeFile(filePath, file, true);
		System.out.println("3.上传 " + filePath + isSuccess(result3));

		

	}

	/**
	 * 获取文件信息
	 */
	public static void testGetFileInfo() {

		// detuyun空间下存在的文件的路径
		String filePath = DIR_ROOT + FILE_NAME;

		System.out.println(filePath + " 的文件信息：" + detuyun.getFileInfo(filePath));
		System.out.println();
	}

	/**
	 * 读取文件/下载文件
	 * 
	 * @throws IOException
	 */
	public static void testReadFile() throws IOException {

		// detuyun空间下存在的文件的路径
		String filePath = DIR_ROOT + FILE_NAME;

		/*
		 * 下载文件，采用数据流模式下载文件（节省内存）
		 */
		// 要写入的本地临时文件
		File file = File.createTempFile("detuyunTempFile_", ".jpg");

		// 把detuyun空间下的文件下载到本地的临时文件
		boolean result = detuyun.readFile(filePath, file);
		System.out.println(filePath + " 下载" + isSuccess(result) + "，保存到 "
				+ file.getAbsolutePath());
		System.out.println();
	}

	/**
	 * 删除文件
	 */
	public static void testDeleteFile() {

		// detuyun空间下存在的文件的路径
		String filePath = DIR_ROOT + FILE_NAME;

		// 删除文件
		boolean result = detuyun.deleteFile(filePath);

		System.out.println(filePath + " 删除" + isSuccess(result));
		System.out.println();
	}

	/**
	 * 创建目录
	 */
	public static void testMkDir() {

		// 方法1：创建一级目录
		String dir1 = DIR_ROOT + FOLDER_NAME;

		boolean result1 = detuyun.mkDir(dir1);
		System.out.println("创建目录：" + dir1 + isSuccess(result1));

		// 方法2：创建多级目录，自动创建父级目录（最多10级）
		String dir2 = DIR_MORE + FOLDER_NAME;

		boolean result2 = detuyun.mkDir(dir2, true);
		System.out.println("自动创建多级目录：" + dir2 + isSuccess(result2));
		System.out.println();
	}

	/**
	 * 读取目录下的文件列表
	 */
	public static void testReadDir() {

		// 参数可以换成其他目录路径
		String dirPath = DIR_ROOT;

		// 读取目录列表，将返回 List 或 NULL
		List<DetuYun.FolderItem> items = detuyun.readDir(dirPath);

		if (null == items) {
			System.out.println("'" + dirPath + "'目录下没有文件。");

		} else {

			for (int i = 0; i < items.size(); i++) {
				System.out.println(items.get(i));
			}

			System.out.println("'" + dirPath + "'目录总共有 " + items.size()
					+ " 个文件。");
		}

		System.out.println();
	}

	/**
	 * 删除目录
	 */
	public static void testRmDir() {

		// 带删除的目录必须存在，并且目录下已不存在任何文件或子目录
		String dirPath = DIR_ROOT + FOLDER_NAME;

		boolean result = detuyun.rmDir(dirPath);

		System.out.println("删除目录：" + dirPath + isSuccess(result));
		System.out.println();
	}

	private static String isSuccess(boolean result) {
		return result ? " 成功" : " 失败";
	}
}
