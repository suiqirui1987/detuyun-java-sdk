package demo;

import java.io.File;
import java.io.IOException;
import com.DetuYun;

/**
 * 图片类空间的demo，一般性操作参考文件空间的demo（FileBucketDemo.java）
 * <p>
 * 注意：直接使用部分图片处理功能后，将会丢弃原图保存处理后的图片
 */
public class PicBucketDemo {

	// 运行前先设置好以下三个参数
	private static final String BUCKET_NAME = "ddd";//"空间名称";
	private static final String USER_NAME = "minuc202";//"Access Key";
	private static final String USER_PWD = "pg2s3azlptl4bgeg52b8vvthruvuh9";//"Access Secret";

	

	/** 根目录 */
	private static final String DIR_ROOT = "/";

	/** 上传到detuyun的图片名 */
	private static final String PIC_NAME = "college.jpg";

	/** 本地待上传的测试文件 */
	private static final String SAMPLE_PIC_FILE = System
			.getProperty("user.dir") + "/college.jpg";

	private static DetuYun detuyun = null;

	static {
		File picFile = new File(SAMPLE_PIC_FILE);

		if (!picFile.isFile()) {
			System.out.println("本地待上传的测试文件不存在!");
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

		/*
		 * 一般性操作参考文件空间的demo（FileBucketDemo.java）
		 * 
		 * 注：图片的所有参数均可以自由搭配使用
		 */

		// 1.上传文件
		testWriteFile();


	}

	/**
	 * 上传文件
	 * 
	 * @throws IOException
	 */
	public static void testWriteFile() throws IOException {

		// 要传到detuyun后的文件路径
		String filePath = DIR_ROOT + PIC_NAME;

		// 本地待上传的图片文件
		File file = new File(SAMPLE_PIC_FILE);

		


		// 上传文件，并自动创建父级目录（最多10级）
		boolean result = detuyun.writeFile(filePath, file, true);

		System.out.println(filePath + " 上传" + isSuccess(result));

		// 获取上传文件后的信息（仅图片空间有返回数据）
		System.out.println("\r\n****** " + file.getName() + " 的图片信息 *******");
		// 图片宽度
		System.out.println("图片宽度:" + detuyun.getPicWidth());
		// 图片高度
		System.out.println("图片高度:" + detuyun.getPicHeight());
		// 图片帧数
		System.out.println("图片帧数:" + detuyun.getPicFrames());

		System.out.println();
	}

	

	private static String isSuccess(boolean result) {
		return result ? " 成功" : " 失败";
	}
}
