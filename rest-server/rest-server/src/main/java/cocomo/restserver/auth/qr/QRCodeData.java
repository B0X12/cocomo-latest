package cocomo.restserver.auth.qr;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import cocomo.restserver.define.Path;
import com.google.zxing.WriterException;

import org.json.simple.JSONObject;

public class QRCodeData {

	private static QRCodeService qrCreate = new QRCodeService();

	public static LocalTime currentTime;
	public static String currentTimeStr;


	@SuppressWarnings("static-access")
	public static void setTime(int fileNum, String userId) throws WriterException, IOException
	{
		// time값 받아오기
		currentTime = LocalTime.now();
		currentTimeStr = String.format("%s", currentTime);

		JSONObject resultJson = setJsonData(userId);

		// 파일위치 및 파일명, 링크주소, 사이즈, 이미지타입
		// 파일명에 라벨링을 안해주면 이미지 갱신이 안됨
		QRCodeService.createQRImage(new File(Path.PATH_QR + "/cocomoQR" + fileNum + ".png")
				, resultJson.toString(), 200, "png");
	}


	public static JSONObject setJsonData(String userId)
	{
		JSONObject json = new JSONObject();

		json.put("identifier", "cocomo"); // 프로젝트명과
		json.put("userId", userId); // 요청한 User ID로 식별
		json.put("timeStamp", currentTimeStr); // 현재 시간도 넘겨줌

		return json;
	}

}
