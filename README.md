
此SDK适用于Java 6及以上版本。基于得图云存储HTTP REST API接口 构建。使用此 SDK 构建您的网络应用程序，能让您以非常便捷地方式将数据安全地存储到得图云存储上。无论您的网络应用是一个网站程序，还是包括从云端（服务端程序）到终端（手持设备应用）的架构的服务或应用，通过得图云存储及其 SDK，都能让您应用程序的终端用户高速上传和下载，同时也让您的服务端更加轻盈。


- [应用接入](#install)
	- [获取Access Key 和 Secret Key](#acc-appkey)
- [使用说明](#detuyun-api)
	- [1 初始化DetuYun](#detuyun-init)
	- [2 上传文件](#detuyun-upload)
	- [3 下载文件](#detuyun-down)
	- [4 创建目录](#detuyun-createdir)
	- [5 删除目录或者文件](#detuyun-deletedir)
	- [6 获取目录文件列表](#detuyun-getdir)
	- [7 获取文件信息](#detuyun-getfile)
	- [8 获取空间使用状况](#detuyun-getused)
- [异常处理](#detuyun-exception)


<a name="install"></a>
## 应用接入

<a name="acc-appkey"></a>

### 1. 获取Access Key 和 Secret Key

要接入得图云存储，您需要拥有一对有效的 Access Key 和 Secret Key 用来进行签名认证。可以通过如下步骤获得：

1. <a href="http://www.detuyun.com/user/accesskey" target="_blank">登录得图云开发者自助平台，查看 Access Key 和 Secret Key 。</a>

<a name=detuyun-api></a>
## 使用说明

<a name="detuyun-init"></a>
### 1.初始化DetuYun

	public DetuYun(String bucketName, String userName, String password) {
		this.bucketName = bucketName;
		this.userName = userName;
		this.password = md5(password);
	}
参数`bucketName`为空间名称，`usernameName`为Access Key，`password`为Access Secret。

示例代码如下：

	// 运行前先设置好以下三个参数
	private static final String BUCKET_NAME = "abcdd";//"空间名称";
	private static final String USER_NAME = "faith196";//"Access Key";
	private static final String USER_PWD = "fhx442gh1n1qmeuqyvmtf5nt2uk482";//"Access Secret";
       ...
		// 初始化空间
		detuyun = new DetuYun(BUCKET_NAME, USER_NAME, USER_PWD);

**超时时间设置**
在初始化DetuYun上传时，可以选择设置上传请求超时时间（默认30s）:

	public void setTimeout(int second) {
		this.timeout = second * 1000;
	}

<a name="detuyun-upload"></a>
### 2. 上传文件

		boolean result3 = detuyun.writeFile(filePath, file, true);
		System.out.println("3.上传 " + filePath + isSuccess(result3));

采用数据流模式上传文件（节省内存），可自动创建父级目录（最多10级）。第三个参数为可选。True 表示自动创建相应目录，默认值为False。

文件空间上传成功后返回`True`，图片空间上传成功后一数组形式返回图片信息：

				picWidth = conn.getHeaderField(X_DETUYUN_WIDTH);
				picHeight = conn.getHeaderField(X_DETUYUN_HEIGHT);
				picFrames = conn.getHeaderField(X_DETUYUN_FRAMES);
				picType = conn.getHeaderField(X_DETUYUN_FILE_TYPE);
如果上传失败，则会抛出异常。

<a name=detuyun-down></a>
### 3. 下载文件

		boolean result = detuyun.readFile(filePath, file);
		System.out.println(filePath + " 下载" + isSuccess(result) + "，保存到 "
				+ file.getAbsolutePath());
		System.out.println();

直接获取文件时，返回文件内容，使用数据流形式获取时，成功返回True。 如果获取文件失败，则抛出异常。

<a name=detuyun-createdir></a>
### 4.创建目录

		// 方法1：创建一级目录
		String dir1 = DIR_ROOT + FOLDER_NAME;

		boolean result1 = detuyun.mkDir(dir1);
		System.out.println("创建目录：" + dir1 + isSuccess(result1));

		// 方法2：创建多级目录，自动创建父级目录（最多10级）
		String dir2 = DIR_MORE + FOLDER_NAME;

		boolean result2 = detuyun.mkDir(dir2, true);
		System.out.println("自动创建多级目录：" + dir2 + isSuccess(result2));
		System.out.println();

目录路径必须以斜杠 `/` 结尾，创建成功返回 `True`，否则抛出异常。


<a name=detuyun-deletedir></a>
### 5.删除目录或者文件

		boolean result = detuyun.deleteFile(filePath);
		System.out.println(filePath + " 删除" + isSuccess(result));
		System.out.println();
		
删除成功返回True，否则抛出异常。注意删除目录时，`必须保证目录为空` ，否则也会抛出异常。

<a name=detuyun-getdir></a>
### 6.获取目录文件列表
	public FolderItem(String data) {
		String[] a = data.split("\t");
			...
		}

		...

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
获取目录文件以及子目录列表。需要获取根目录列表，使用 `data.split("\t")` 分隔获取相应内容，返回结果为一个数组。目录获取失败则抛出异常。

### 7.获取文件信息

	System.out.println(filePath + " 的文件信息：" + detuyun.getFileInfo(filePath));
		System.out.println();

		...

	// 判断是否存在文件信息
		if (isEmpty(fileType) && isEmpty(fileSize) && isEmpty(fileDate)) {
			return null;
		}

		Map<String, String> mp = new HashMap<String, String>();
		mp.put("type", fileType);
		mp.put("size", fileSize);
		mp.put("date", fileDate);

		return mp;

<a name=detuyun-getused></a>
###8.获取空间使用状况

		long usage = detuyun.getBucketUsage();
		System.out.println("空间总使用量：" + usage + "kb");
		System.out.println();
返回的结果为空间使用量，单位 kb。

## 异常处理
当API请求发生错误时，SDK将抛出异常，具体错误代码请参考 <a target="_blank"  href="http://www.detuyun.com/docs/page6.html">标准API错误代码表</a>

根据返回HTTP CODE的不同，SDK将抛出以下异常：

* **DetuYunAuthorizationException** 401，授权错误
* **DetuYunForbiddenException** 403，权限错误
* **DetuYunNotFoundException** 404，文件或目录不存在
* **DetuYunNotAcceptableException** 406， 目录错误
* **DetuYunServiceUnavailable** 503，系统错误

未包含在以上异常中的错误，将统一抛出 `DetuYunException` 异常。

为了真确处理API请求中可能出现的异常，建议将API操作放在`try{...}catch(Exception $e){…}` 块中
