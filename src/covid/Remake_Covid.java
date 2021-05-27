package covid;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class Remake_Covid extends PrivacyKey{
	private static Image img = null;
	private static Image imgloc = null;
	
	public static void main(String[] args) throws Exception {
        int width = 450;
        int height = 500;
    	String xml;
    	
		SimpleDateFormat date = new SimpleDateFormat("YYYYMMdd");
		Calendar today = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();
		
		yesterday.add(Calendar.DATE, -2);
		
		String todayin = date.format(today.getTime());
		String yesterdayin = date.format(yesterday.getTime());
		
		System.out.println(yesterdayin);
		System.out.println(todayin);
		
        String addr = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey="+key+"&pageNo=1&numOfRows=10&startCreateDt="+yesterdayin+"&endCreateDt="+todayin;
        URL url = new URL(addr);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setConnectTimeout(10000);
        http.setUseCaches(false);

        // 위 부분 까지는 주소만 변경되고 모든 경우 동일

        // 위 주소에서 주는 데이터를 문자열로 읽기 위한 스트림 객체 생성
        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            sb.append(line);
        }
        xml = sb.toString();
        br.close();
        http.disconnect();
        
        xml = sb.toString();
        
        if (xml != null) {
	        DocumentBuilderFactory factory = DocumentBuilderFactory
	                .newInstance();
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	
	        // xml 문자열은 InputStream으로 변환
	        InputStream is =new ByteArrayInputStream(xml.getBytes());
	        // 파싱 시작
	        Document doc = documentBuilder.parse(is);
	        // 최상위 노드 찾기
	        Element element = doc.getDocumentElement();
			
	//		Nodes
	//		해외유입, 지역감염, 총 감염자, 격리중, 격리해제, 사망
			NodeList [] infestinfo = {
				element.getElementsByTagName("gubun"),
				element.getElementsByTagName("overFlowCnt"),
				element.getElementsByTagName("localOccCnt"),
				element.getElementsByTagName("defCnt"),
		        element.getElementsByTagName("isolIngCnt"),
		        element.getElementsByTagName("isolClearCnt"),
		        element.getElementsByTagName("deathCnt"),
		        element.getElementsByTagName("incDec"),
		        element.getElementsByTagName("stdDay")
			};
			
			String [] category = {
				"지역이름: ",
				"해외유입: ",
				"지역감염: ",
				"감염자수: ",
				"격리환자: ",
				"격리해제: ",
				"사망자수: ",
				"신규확진: ",
				"기록일자: "
			};
			
	        BufferedImage covidbuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = covidbuff.createGraphics();
	        
	        img = ImageIO.read(new File("backg/covidbg.png"));
    		g2d.drawImage(img,0,0,width,height, null);
	        
    		// 확진환자
    		String ttv = infestinfo[3].item(18).getFirstChild().getNodeValue();
    		
		    Graphics2D ttvi = covidbuff.createGraphics();
			ttvi.setColor(Color.WHITE);
			ttvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
		    FontMetrics ttvir = ttvi.getFontMetrics();
		    int ttvix = ((width - ttvir.stringWidth(ttv)-59));
		    
    		ttvi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		ttvi.drawString(ttv, 0 + ttvix, 118);
    		
    		// 국내환자, 해외환자
    		for (int i = 2; i > 0; i--) {
    			String tlv = infestinfo[i].item(18).getFirstChild().getNodeValue();
    			
    		    Graphics2D tlvi = covidbuff.createGraphics();
    			tlvi.setColor(Color.WHITE);
    			tlvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics tlvir = tlvi.getFontMetrics();
    		    int tlvix = ((width - tlvir.stringWidth(tlv)-59));
    		    
        		tlvi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		tlvi.drawString(tlv, 0 + tlvix, 236-(i*38));
        		
        		double tlvd = Double.parseDouble(tlv);
        		
    			Graphics2D tlvdia = covidbuff.createGraphics();
        		tlvdia.setColor(new Color(255, 115, 118));
        		tlvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		tlvdia.setRenderingHint(
        				RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		
        		if (tlvd > 0) {
    	    		tlvdia.drawString("▲", 401, 238-(i*38));
        		}
        		else
        		{
    	    		tlvdia.drawString("-", 411, 234-(i*38));
        		}
			}
    		
    		// 격리자, 격리해제, 사망 반복문
    		
    		Graphics2D tdvi = covidbuff.createGraphics();
    		tdvi.setColor(Color.WHITE);
    		tdvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
    		FontMetrics tdvir = tdvi.getFontMetrics();
    		
    		for (int i = 0; i < 3; i++) {
    			String index = infestinfo[i+4].item(18).getFirstChild().getNodeValue();
    			
			    int tdvix = ((width - tdvir.stringWidth(index)-59));
			    
	    		tdvi.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		tdvi.drawString(index, 0 + tdvix, 249+(i*84));
        	}
 		
    		// 격리환자 증가
    		Color [] colours = new Color[3];
    		
    		colours[0] = new Color(255, 233, 0);
    		colours[1] = new Color(112, 208, 236);
    		colours[2] = new Color(193, 155, 223);
    		
    		for (int i = 0; i < 3; i++) {
	    		double todayiv = Double.parseDouble(infestinfo[i+4].item(18).getFirstChild().getNodeValue());
	    		double yesterdayiv = Double.parseDouble(infestinfo[i+4].item(37).getFirstChild().getNodeValue());
	    		double yivd = todayiv-yesterdayiv;
	    		
	    		long yivdn = Math.round(yivd);
	    		String yivdm = Long.toString(yivdn);
	    		
	    		if (yivdn < 0) {
	    			yivdm = Long.toString(yivdn*-1);
	    		}
	    		
			    Graphics2D yivdi = covidbuff.createGraphics();
				yivdi.setColor(Color.WHITE);
				yivdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
			    FontMetrics yivdir = yivdi.getFontMetrics();
			    int yivdix = ((width - yivdir.stringWidth(yivdm)-59));
			    
	    		yivdi.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		yivdi.drawString(yivdm, 0 + yivdix, 284+(i*84));
	    		
	    		Graphics2D yivdia = covidbuff.createGraphics();
	    		yivdia.setColor(colours[i]);
	    		yivdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		yivdia.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		
	    		if (yivd > 0) {
		    		yivdia.drawString("▲", 401, 286+(i*84));
	    		}
	    		else if (yivd == 0)
	    		{
	        		yivdia.drawString("-", 411, 282+(i*84));
	    		}
	    		else
	    		{
	        		yivdia.drawString("▼", 401, 286+(i*84));
	    		}
    		}
    			
    		File file = new File("covidvrc.png");
    		file.delete();
    		ImageIO.write(covidbuff, "png", file);
    		
//			모든 내용
			System.out.println("\n\n지역별");
			for (int i = 0; i < 19; i++) {
				System.out.println("\n번호: "+i);
				for (int j = 0; j < infestinfo.length; j++) {
					Node target = infestinfo[j].item(i);
					Node laser = target.getFirstChild();
					String tars = laser.getNodeValue();
					
					Node targety = infestinfo[j].item(i+19);
					Node lasery = targety.getFirstChild();
					String tarsy = lasery.getNodeValue();
					System.out.println(category[j]+tars+", "+tarsy);
				}
			}
			
//			지역별 총 감염자 수
	        BufferedImage covidbuffloc = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D backgloc = covidbuffloc.createGraphics();
	        imgloc = ImageIO.read(new File("backg/sidecovid.png"));
    		backgloc.drawImage(imgloc,0,0,width,height, null);
			
			for (int i = 17; i >= 0; i--) {
				String getNode_localIn = infestinfo[7].item(i).getFirstChild().getNodeValue();
				String getNode_localNm = infestinfo[0].item(i).getFirstChild().getNodeValue();
				
				System.out.println("\n번호: "+i);
				System.out.println("지역: "+getNode_localNm);
				System.out.println("확진: "+getNode_localIn);
				
				Graphics2D imglocal = covidbuffloc.createGraphics();
				imglocal.setColor(Color.WHITE);
				imglocal.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
				FontMetrics imglocalfm = imglocal.getFontMetrics();
				
				Graphics2D imgInc = covidbuffloc.createGraphics();
				imgInc.setColor(new Color(255, 80, 80));
				imgInc.setFont(new Font("서울남산체 B", Font.BOLD, 24));
				
				int imglocalright;
				String up = "-";
				
				double Local = Double.parseDouble(getNode_localIn);
				
				if (Local > 0) {
					up = "▲";
				}
				
				imgInc.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				if ((i % 2) == 0) {
					imglocalright = ((width - imglocalfm.stringWidth(getNode_localIn)-45));
					imglocal.setRenderingHint(
							RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					imglocal.drawString(getNode_localIn, 0 + imglocalright, 393+(-18*(i)));
					imgInc.drawString(up, 180, 374+(-18*(i-1)));
				} else {
					imglocalright = ((width - imglocalfm.stringWidth(getNode_localIn)-275));
					imglocal.setRenderingHint(
							RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					imglocal.drawString(getNode_localIn, 0 + imglocalright, 393+(-18*(i-1)));
					imgInc.drawString(up, 408, 374+(-18*(i-2)));
				}
				
			}
			
    		File file2 = new File("covidvrc1.png");
    		file2.delete();
    		ImageIO.write(covidbuffloc, "png", file2);
		}
	}
}
