package covid;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

public class covid19 extends PrivacyKey{
	
	private static Image img = null;
	private static Image imgloc = null;
	
	public static void main(String[] args) throws Exception {
		
        int width = 450;
        int height = 500;
    	String xml;
    	
    	Date d1 = new Date();
		Date d2 = new Date();
				
		int d1y = (d1.getYear()+1900);

		String d1m1 = null;
		String d1d1 = null;
		
		String d2m2 = null;
		String d2d2 = null;
		
	    Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.DATE, -2);
		
	    // Today
		if (d1.getMonth()+1 < 10)
		{
			d1m1 = "0"+(d1.getMonth()+1);
		}
		else
		{
			d1m1 = ""+(d1.getMonth()+1);
		}
		
		if (d1.getDate() < 10)
		{
			d1d1 = "0"+(d1.getDate());
		}
		else
		{
			d1d1 = ""+(d1.getDate());
		}
		
		
		// Yesterday
		if (d2.getMonth()+1 < 10)
		{
			d2m2 = "0"+(cal.get(Calendar.MONTH)+1);
		}
		else
		{
			d2m2 = ""+(cal.get(Calendar.MONTH)+1);
		}
		
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
		{
			d2d2 = "0"+(cal.get(Calendar.DAY_OF_MONTH));
		}
		else
		{
			d2d2 = ""+(cal.get(Calendar.DAY_OF_MONTH));
		}
		
		String yesterdayin = (cal.get(Calendar.YEAR)+""+d2m2+""+d2d2);
		String todayin = (d1y+""+d1m1+""+d1d1);
		
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
	        // 원하는 태그 데이터 찾아오기
	        NodeList oversea = element.getElementsByTagName("overFlowCnt");
	        NodeList local = element.getElementsByTagName("localOccCnt");
	        NodeList total = element.getElementsByTagName("defCnt");
	        NodeList isoling = element.getElementsByTagName("isolIngCnt");
	        NodeList isolclear = element.getElementsByTagName("isolClearCnt");
	        NodeList death = element.getElementsByTagName("deathCnt");
	        NodeList Increase = element.getElementsByTagName("incDec");
	        
	        // 오늘 총합
	        Node todaytotal = total.item(18);
	        Node tt = todaytotal.getFirstChild();
	        String ttv = tt.getNodeValue();
	        
	        // 오늘 국내
	        Node todaylocal = local.item(18);
	        Node tl = todaylocal.getFirstChild();
	        String tlv = tl.getNodeValue();
	        
	        // 오늘 해외
	        Node todayoversea = oversea.item(18);
	        Node to = todayoversea.getFirstChild();
	        String tov = to.getNodeValue();
	        
	        // 오늘 격리
	        Node todayisoling = isoling.item(18);
	        Node ti = todayisoling.getFirstChild();
	        String tiv = ti.getNodeValue();
	        
	        // 오늘 격리해제
	        Node todayisolclear = isolclear.item(18);
	        Node tic = todayisolclear.getFirstChild();
	        String ticv = tic.getNodeValue();
	        
	        // 오늘 사망자
	        Node todaydeath = death.item(18);
	        Node td = todaydeath.getFirstChild();
	        String tdv = td.getNodeValue();
        
	        // 어제 격리
	        Node yesisoling = isoling.item(37);
	        Node yi = yesisoling.getFirstChild();
	        String yiv = yi.getNodeValue();
	        
	        // 어제 격리해제
	        Node yesisolclear = isolclear.item(37);
	        Node yic = yesisolclear.getFirstChild();
	        String yicv = yic.getNodeValue();
	        
	        // 어제 사망자
	        Node yesdeath = death.item(37);
	        Node yd = yesdeath.getFirstChild();
	        String ydv = yd.getNodeValue();
	             
	        BufferedImage covidbuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = covidbuff.createGraphics();
	        
	        img = ImageIO.read(new File("backg/covidbg.png"));
    		g2d.drawImage(img,0,0,width,height, null);
	        
    		// 확진환자
		    Graphics2D ttvi = covidbuff.createGraphics();
			ttvi.setColor(Color.WHITE);
			ttvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
		    FontMetrics ttvir = ttvi.getFontMetrics();
		    int ttvix = ((width - ttvir.stringWidth(ttv)-59));
		    
    		ttvi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		ttvi.drawString(ttv, 0 + ttvix, 118);
    		
    		// 국내환자
		    Graphics2D tlvi = covidbuff.createGraphics();
			tlvi.setColor(Color.WHITE);
			tlvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
		    FontMetrics tlvir = tlvi.getFontMetrics();
		    int tlvix = ((width - tlvir.stringWidth(tlv)-59));
		    
    		tlvi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		tlvi.drawString(tlv, 0 + tlvix, 160);
    		
    		double tlvd = Double.parseDouble(tlv);
    		
    		if (tlvd > 0) {
    			Graphics2D tlvdia = covidbuff.createGraphics();
	    		tlvdia.setColor(new Color(255, 115, 118));
	    		tlvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		tlvdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		tlvdia.drawString("▲", 401, 160);
    		}
    		else
    		{
    			Graphics2D tlvdia = covidbuff.createGraphics();
	    		tlvdia.setColor(new Color(255, 115, 118));
	    		tlvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		tlvdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		tlvdia.drawString("-", 411, 156);
    		}
    		
    		// 해외환자
		    Graphics2D tovi = covidbuff.createGraphics();
			tovi.setColor(Color.WHITE);
			tovi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
		    FontMetrics tovir = tovi.getFontMetrics();
		    int tovix = ((width - tovir.stringWidth(tov)-59));
		    
    		tovi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		tovi.drawString(tov, 0 + tovix, 198);
    		
    		double tovd = Double.parseDouble(tov);
    		
    		if (tovd > 0) {
    			Graphics2D tovdia = covidbuff.createGraphics();
	    		tovdia.setColor(new Color(255, 115, 118));
	    		tovdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		tovdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		tovdia.drawString("▲", 401, 198);
    		}
    		else
    		{
    			Graphics2D tovdia = covidbuff.createGraphics();
	    		tovdia.setColor(new Color(255, 115, 118));
	    		tovdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		tovdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		tovdia.drawString("-", 411, 194);
    		}
    		
    		// 격리환자
		    Graphics2D tivi = covidbuff.createGraphics();
			tivi.setColor(Color.WHITE);
			tivi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
		    FontMetrics tivir = tivi.getFontMetrics();
		    int tivix = ((width - tivir.stringWidth(tiv)-59));
		    
    		tivi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		tivi.drawString(tiv, 0 + tivix, 249);
    		
    		// 격리해체환자
		    Graphics2D ticvi = covidbuff.createGraphics();
			ticvi.setColor(Color.WHITE);
			ticvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
		    FontMetrics ticvir = ticvi.getFontMetrics();
		    int ticvix = ((width - ticvir.stringWidth(ticv)-59));
		    
    		ticvi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		ticvi.drawString(ticv, 0 + ticvix, 333);
    		
    		// 사망자
		    Graphics2D tdvi = covidbuff.createGraphics();
			tdvi.setColor(Color.WHITE);
			tdvi.setFont(new Font("서울남산 장체 B", Font.BOLD, 48));
		    FontMetrics tdvir = tdvi.getFontMetrics();
		    int tdvix = ((width - tdvir.stringWidth(tdv)-59));
		    
    		tdvi.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		tdvi.drawString(tdv, 0 + tdvix, 417);
 		
    		// 격리환자 증가
    		double todayiv = Double.parseDouble(tiv.toString());
    		double yesterdayiv = Double.parseDouble(yiv.toString());
    		double yivd = todayiv-yesterdayiv;
    		
    		long yivdn = Math.round(yivd);
    		String yivdm;
    		
    		if (yivd > 0) {
        		yivdm = Long.toString(yivdn);
    			
			    Graphics2D yivdi = covidbuff.createGraphics();
				yivdi.setColor(Color.WHITE);
				yivdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
			    FontMetrics yivdir = yivdi.getFontMetrics();
			    int yivdix = ((width - yivdir.stringWidth(yivdm)-59));
			    
	    		yivdi.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		yivdi.drawString(yivdm, 0 + yivdix, 284);
	    		
	    		Graphics2D yivdia = covidbuff.createGraphics();
	    		yivdia.setColor(new Color(255, 233, 0));
	    		yivdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		yivdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		yivdia.drawString("▲", 401, 286);
    		}
    		else if (yivd == 0)
    		{
        		yivdm = Long.toString(yivdn);
    			
    		    Graphics2D yivdi = covidbuff.createGraphics();
    			yivdi.setColor(Color.WHITE);
    			yivdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics yivdir = yivdi.getFontMetrics();
    		    int yivdix = ((width - yivdir.stringWidth(yivdm)-59));
    		    
        		yivdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yivdi.drawString(yivdm, 0 + yivdix, 284);
        		
        		Graphics2D yivdia = covidbuff.createGraphics();
        		yivdia.setColor(new Color(255, 233, 0));
        		yivdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		yivdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yivdia.drawString("-", 411, 282);
    		}
    		else
    		{
        		yivdm = Long.toString(yivdn*-1);
    			
    		    Graphics2D yivdi = covidbuff.createGraphics();
    			yivdi.setColor(Color.WHITE);
    			yivdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics yivdir = yivdi.getFontMetrics();
    		    int yivdix = ((width - yivdir.stringWidth(yivdm)-59));
    		    
        		yivdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yivdi.drawString(yivdm, 0 + yivdix, 284);
        		
        		Graphics2D yivdia = covidbuff.createGraphics();
        		yivdia.setColor(new Color(255, 233, 0));
        		yivdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		yivdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yivdia.drawString("▼", 401, 286);
    		}
    			
    		
    		// 격리해체환자 증가
    		double todayicv = Double.parseDouble(ticv.toString());
    		double yesterdayicv = Double.parseDouble(yicv.toString());
    		double yicvd = todayicv-yesterdayicv;
    		
    		long yicvdn = Math.round(yicvd);
    		String yicvdm;
    		
    		if (yicvd > 0) {
        		yicvdm = Long.toString(yicvdn);
    			
			    Graphics2D yicvdi = covidbuff.createGraphics();
				yicvdi.setColor(Color.WHITE);
				yicvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
			    FontMetrics yicvdir = yicvdi.getFontMetrics();
			    int yicvdix = ((width - yicvdir.stringWidth(yicvdm)-59));
			    
	    		yicvdi.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		yicvdi.drawString(yicvdm, 0 + yicvdix, 368);
	    		
	    		Graphics2D yicvdia = covidbuff.createGraphics();
	    		yicvdia.setColor(new Color(112, 208, 236));
	    		yicvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		yicvdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		yicvdia.drawString("▲", 401, 368);
    		}
    		else if (yicvd == 0)
    		{
        		yicvdm = Long.toString(yicvdn);
    			
    		    Graphics2D yicvdi = covidbuff.createGraphics();
    			yicvdi.setColor(Color.WHITE);
    			yicvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics yicvdir = yicvdi.getFontMetrics();
    		    int yicvdix = ((width - yicvdir.stringWidth(yicvdm)-59));
    		    
        		yicvdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yicvdi.drawString(yicvdm, 0 + yicvdix, 368);
        		
        		Graphics2D yicvdia = covidbuff.createGraphics();
	    		yicvdia.setColor(new Color(112, 208, 236));
        		yicvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		yicvdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yicvdia.drawString("-", 411, 366);
    		}
    		else
    		{
        		yicvdm = Long.toString(yicvdn*-1);
    			
    		    Graphics2D yicvdi = covidbuff.createGraphics();
    			yicvdi.setColor(Color.WHITE);
    			yicvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics yicvdir = yicvdi.getFontMetrics();
    		    int yicvdix = ((width - yicvdir.stringWidth(yicvdm)-59));
    		    
        		yicvdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yicvdi.drawString(yicvdm, 0 + yicvdix, 368);
        		
        		Graphics2D yicvdia = covidbuff.createGraphics();
	    		yicvdia.setColor(new Color(112, 208, 236));
        		yicvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		yicvdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		yicvdia.drawString("▼", 401, 368);
    		}
    		
    		// 사망자 증가
    		double todaydv = Double.parseDouble(tdv.toString());
    		double yesterdaydv = Double.parseDouble(ydv.toString());
    		double ydvd = todaydv-yesterdaydv;
    		
    		long ydvdn = Math.round(ydvd);
    		String ydvdm;
    		
    		if (ydvd > 0) {
        		ydvdm = Long.toString(ydvdn);
    			
			    Graphics2D ydvdi = covidbuff.createGraphics();
				ydvdi.setColor(Color.WHITE);
				ydvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
			    FontMetrics ydvdir = ydvdi.getFontMetrics();
			    int ydvdix = ((width - ydvdir.stringWidth(ydvdm)-59));
			    
	    		ydvdi.setRenderingHint(
	        			RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		ydvdi.drawString(ydvdm, 0 + ydvdix, 452);
	    		
	    		Graphics2D ydvdia = covidbuff.createGraphics();
	    		ydvdia.setColor(new Color(193, 155, 223));
	    		ydvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
	    		
	    		ydvdia.setRenderingHint(
	    				RenderingHints.KEY_TEXT_ANTIALIASING,
	        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    		ydvdia.drawString("▲", 401, 452);
    		}
    		else if (ydvd == 0)
    		{
        		ydvdm = Long.toString(ydvdn);
    			
    		    Graphics2D ydvdi = covidbuff.createGraphics();
    			ydvdi.setColor(Color.WHITE);
    			ydvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics ydvdir = ydvdi.getFontMetrics();
    		    int ydvdix = ((width - ydvdir.stringWidth(ydvdm)-59));
    		    
        		ydvdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		ydvdi.drawString(ydvdm, 0 + ydvdix, 452);
        		
        		Graphics2D ydvdia = covidbuff.createGraphics();
	    		ydvdia.setColor(new Color(193, 155, 223));
        		ydvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		ydvdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		ydvdia.drawString("-", 411, 450);
    		}
    		else
    		{
        		ydvdm = Long.toString(ydvdn*-1);
    			
    		    Graphics2D ydvdi = covidbuff.createGraphics();
    			ydvdi.setColor(Color.WHITE);
    			ydvdi.setFont(new Font("서울남산 장체 B", Font.BOLD, 32));
    		    FontMetrics ydvdir = ydvdi.getFontMetrics();
    		    int ydvdix = ((width - ydvdir.stringWidth(ydvdm)-59));
    		    
        		ydvdi.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		ydvdi.drawString(ydvdm, 0 + ydvdix, 452);
        		
        		Graphics2D ydvdia = covidbuff.createGraphics();
	    		ydvdia.setColor(new Color(193, 155, 223));
        		ydvdia.setFont(new Font("서울남산체 B", Font.BOLD, 32));
        		
        		ydvdia.setRenderingHint(
            			RenderingHints.KEY_TEXT_ANTIALIASING,
            			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		ydvdia.drawString("▼", 401, 452);
    		}
    		
    		File file = new File("covidvrc_old.png");
    		ImageIO.write(covidbuff, "png", file);
    		
    		System.out.println("<corona>");
    		System.out.println("\t<defCnt>"+ttv+"</defCnt>");
	        System.out.println("\t<overFlowCnt>"+tov+"</overFlowCnt>");
	        System.out.println("\t<localOccCnt>"+tlv+"</localOccCnt>");
	        System.out.println("\t<isolIngCnt>"+tiv+"</isolIngCnt>");
	        System.out.println("\t<isolClearCnt>"+ticv+"</isolClearCnt>");
	        System.out.println("\t<deathCnt>"+tdv+"</deathCnt>");
    		System.out.println("</corona>");
    		// 오늘 검역
	        Node todayflow = Increase.item(0);
	        Node todayflow1 = todayflow.getFirstChild();
	        String todayflowcnt = todayflow1.getNodeValue();
    		
	        // 오늘 제주
	        Node todayjeju = Increase.item(1);
	        Node todayjeju1 = todayjeju.getFirstChild();
	        String todayjejucnt = todayjeju1.getNodeValue();
	        
	        // 오늘 경남
	        Node todaykn = Increase.item(2);
	        Node todaykn1 = todaykn.getFirstChild();
	        String todaykncnt = todaykn1.getNodeValue();
	        
	        // 오늘 경북
	        Node todaykb = Increase.item(3);
	        Node todaykb1 = todaykb.getFirstChild();
	        String todaykbcnt = todaykb1.getNodeValue();
	        
	        // 오늘 전남
	        Node todayjn = Increase.item(4);
	        Node todayjn1 = todayjn.getFirstChild();
	        String todayjncnt = todayjn1.getNodeValue();
	        
	        // 오늘 전북
	        Node todayjb = Increase.item(5);
	        Node todayjb1 = todayjb.getFirstChild();
	        String todayjbcnt = todayjb1.getNodeValue();
	        
	        // 오늘 충남
	        Node todaycn = Increase.item(6);
	        Node todaycn1 = todaycn.getFirstChild();
	        String todaycncnt = todaycn1.getNodeValue();
	        
	        // 오늘 충북
	        Node todaycb = Increase.item(7);
	        Node todaycb1 = todaycb.getFirstChild();
	        String todaycbcnt = todaycb1.getNodeValue();
	        
	        // 오늘 강원
	        Node todaykw = Increase.item(8);
	        Node todaykw1 = todaykw.getFirstChild();
	        String todaykwcnt = todaykw1.getNodeValue();
	        
	        // 오늘 경기
	        Node todaygg = Increase.item(9);
	        Node todaygg1 = todaygg.getFirstChild();
	        String todayggcnt = todaygg1.getNodeValue();
	        
	        // 오늘 세종
	        Node todaysj = Increase.item(10);
	        Node todaysj1 = todaysj.getFirstChild();
	        String todaysjcnt = todaysj1.getNodeValue();
	        
	        // 오늘 울산
	        Node todayus = Increase.item(11);
	        Node todayus1 = todayus.getFirstChild();
	        String todayuscnt = todayus1.getNodeValue();
	        
	        // 오늘 대전
	        Node todaydj = Increase.item(12);
	        Node todaydj1 = todaydj.getFirstChild();
	        String todaydjcnt = todaydj1.getNodeValue();
	        
	        // 오늘 광주
	        Node todaygj = Increase.item(13);
	        Node todaygj1 = todaygj.getFirstChild();
	        String todaygjcnt = todaygj1.getNodeValue();
	        
	        // 오늘 인천
	        Node todayic = Increase.item(14);
	        Node todayic1 = todayic.getFirstChild();
	        String todayiccnt = todayic1.getNodeValue();
	        
	        // 오늘 대구
	        Node todaydg = Increase.item(15);
	        Node todaydg1 = todaydg.getFirstChild();
	        String todaydgcnt = todaydg1.getNodeValue();
	        
	        // 오늘 부산
	        Node todaybs = Increase.item(16);
	        Node todaybs1 = todaybs.getFirstChild();
	        String todaybscnt = todaybs1.getNodeValue();
	        
	        // 오늘 서울
	        Node todayso = Increase.item(17);
	        Node todayso1 = todayso.getFirstChild();
	        String todaysocnt = todayso1.getNodeValue();
	        
    		// 어제 검역
	        Node yesterdayflow = Increase.item(19);
	        Node yesterdayflow1 = yesterdayflow.getFirstChild();
	        String yesterdayflowcnt = yesterdayflow1.getNodeValue();
	        
	        // 어제 제주
	        Node yesterdayjeju = Increase.item(20);
	        Node yesterdayjeju1 = yesterdayjeju.getFirstChild();
	        String yesterdayjejucnt = yesterdayjeju1.getNodeValue();
	        
	        // 어제 경남
	        Node yesterdaykn = Increase.item(21);
	        Node yesterdaykn1 = yesterdaykn.getFirstChild();
	        String yesterdaykncnt = yesterdaykn1.getNodeValue();
	        
	        // 어제 경북
	        Node yesterdaykb = Increase.item(22);
	        Node yesterdaykb1 = yesterdaykb.getFirstChild();
	        String yesterdaykbcnt = yesterdaykb1.getNodeValue();
	        
	        // 어제 전남
	        Node yesterdayjn = Increase.item(23);
	        Node yesterdayjn1 = yesterdayjn.getFirstChild();
	        String yesterdayjncnt = yesterdayjn1.getNodeValue();
	        
	        // 어제 전북
	        Node yesterdayjb = Increase.item(24);
	        Node yesterdayjb1 = yesterdayjb.getFirstChild();
	        String yesterdayjbcnt = yesterdayjb1.getNodeValue();
	        
	        // 어제 충남
	        Node yesterdaycn = Increase.item(25);
	        Node yesterdaycn1 = yesterdaycn.getFirstChild();
	        String yesterdaycncnt = yesterdaycn1.getNodeValue();
	        
	        // 어제 충북
	        Node yesterdaycb = Increase.item(26);
	        Node yesterdaycb1 = yesterdaycb.getFirstChild();
	        String yesterdaycbcnt = yesterdaycb1.getNodeValue();
	        
	        // 어제 강원
	        Node yesterdaykw = Increase.item(27);
	        Node yesterdaykw1 = yesterdaykw.getFirstChild();
	        String yesterdaykwcnt = yesterdaykw1.getNodeValue();
	        
	        // 어제 경기
	        Node yesterdaygg = Increase.item(28);
	        Node yesterdaygg1 = yesterdaygg.getFirstChild();
	        String yesterdayggcnt = yesterdaygg1.getNodeValue();
	        
	        // 어제 세종
	        Node yesterdaysj = Increase.item(29);
	        Node yesterdaysj1 = yesterdaysj.getFirstChild();
	        String yesterdaysjcnt = yesterdaysj1.getNodeValue();
	        
	        // 어제 울산
	        Node yesterdayus = Increase.item(30);
	        Node yesterdayus1 = yesterdayus.getFirstChild();
	        String yesterdayuscnt = yesterdayus1.getNodeValue();
	        
	        // 어제 대전
	        Node yesterdaydj = Increase.item(31);
	        Node yesterdaydj1 = yesterdaydj.getFirstChild();
	        String yesterdaydjcnt = yesterdaydj1.getNodeValue();
	        
	        // 어제 광주
	        Node yesterdaygj = Increase.item(32);
	        Node yesterdaygj1 = yesterdaygj.getFirstChild();
	        String yesterdaygjcnt = yesterdaygj1.getNodeValue();
	        
	        // 어제 인천
	        Node yesterdayic = Increase.item(33);
	        Node yesterdayic1 = yesterdayic.getFirstChild();
	        String yesterdayiccnt = yesterdayic1.getNodeValue();
	        
	        // 어제 대구
	        Node yesterdaydg = Increase.item(34);
	        Node yesterdaydg1 = yesterdaydg.getFirstChild();
	        String yesterdaydgcnt = yesterdaydg1.getNodeValue();
	        
	        // 어제 부산
	        Node yesterdaybs = Increase.item(35);
	        Node yesterdaybs1 = yesterdaybs.getFirstChild();
	        String yesterdaybscnt = yesterdaybs1.getNodeValue();
	        
	        // 어제 서울
	        Node yesterdayso = Increase.item(36);
	        Node yesterdayso1 = yesterdayso.getFirstChild();
	        String yesterdaysocnt = todayso1.getNodeValue();
	        
	        BufferedImage covidbuffloc = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D backgloc = covidbuffloc.createGraphics();
	        imgloc = ImageIO.read(new File("backg/sidecovid.png"));
    		backgloc.drawImage(imgloc,0,0,width,height, null);
    		
    		// 서울
		    Graphics2D imgso0 = covidbuffloc.createGraphics();
		    imgso0.setColor(Color.WHITE);
		    imgso0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgso0fm = imgso0.getFontMetrics();
		    int imgso0right = ((width - imgso0fm.stringWidth(todaysocnt)-300));
		    
		    imgso0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgso0.drawString(todaysocnt, 0 + imgso0right, 106);
    		
			/*
			 * Graphics2D imgsoid = covidbuffloc.createGraphics();
			 * imgsoid.setColor(Color.white); imgsoid.setFont(new Font("서울남산 장체 B",
			 * Font.BOLD, 30));
			 * 
			 * imgsoid.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			 * RenderingHints.VALUE_TEXT_ANTIALIAS_ON); imgsoid.drawString(todaysocnt, 300,
			 * 106);
			 */
    		
    		// 대전
		    Graphics2D imgdj0 = covidbuffloc.createGraphics();
		    imgdj0.setColor(Color.WHITE);
		    imgdj0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgdj0fm = imgdj0.getFontMetrics();
		    int imgdj0right = ((width - imgdj0fm.stringWidth(todaydjcnt)-300));
		    
		    imgdj0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgdj0.drawString(todaydjcnt, 0 + imgdj0right, 142);
    		
    		// 울산
		    Graphics2D imgus0 = covidbuffloc.createGraphics();
		    imgus0.setColor(Color.WHITE);
		    imgus0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgus0fm = imgus0.getFontMetrics();
		    int imgus0right = ((width - imgus0fm.stringWidth(todayuscnt)-300));
		    
		    imgus0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgus0.drawString(todayuscnt, 0 + imgus0right, 178);
    		
    		// 세종
		    Graphics2D imgsj0 = covidbuffloc.createGraphics();
		    imgsj0.setColor(Color.WHITE);
		    imgsj0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgsj0fm = imgsj0.getFontMetrics();
		    int imgsj0right = ((width - imgsj0fm.stringWidth(todaysjcnt)-300));
		    
		    imgsj0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgsj0.drawString(todaysjcnt, 0 + imgsj0right, 214);
    		
    		// 충남
		    Graphics2D imgcn0 = covidbuffloc.createGraphics();
		    imgcn0.setColor(Color.WHITE);
		    imgcn0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgcn0fm = imgcn0.getFontMetrics();
		    int imgcn0right = ((width - imgcn0fm.stringWidth(todaycncnt)-300));
		    
		    imgcn0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgcn0.drawString(todaycncnt, 0 + imgcn0right, 250);
    		
    		// 전남
		    Graphics2D imgjn0 = covidbuffloc.createGraphics();
		    imgjn0.setColor(Color.WHITE);
		    imgjn0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgjn0fm = imgjn0.getFontMetrics();
		    int imgjn0right = ((width - imgjn0fm.stringWidth(todayjncnt)-300));
		    
		    imgjn0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgjn0.drawString(todayjncnt, 0 + imgjn0right, 286);
    		
    		// 경남
		    Graphics2D imgkn0 = covidbuffloc.createGraphics();
		    imgkn0.setColor(Color.WHITE);
		    imgkn0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgkn0fm = imgkn0.getFontMetrics();
		    int imgkn0right = ((width - imgkn0fm.stringWidth(todaykncnt)-300));
		    
		    imgkn0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgkn0.drawString(todaykncnt, 0 + imgkn0right, 322);
    		
    		// 경기
		    Graphics2D imggg0 = covidbuffloc.createGraphics();
		    imggg0.setColor(Color.WHITE);
		    imggg0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imggg0fm = imggg0.getFontMetrics();
		    int imggg0right = ((width - imggg0fm.stringWidth(todayggcnt)-300));
		    
		    imggg0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imggg0.drawString(todayggcnt, 0 + imggg0right, 358);
    		
    		// 제주
		    Graphics2D imgjeju0 = covidbuffloc.createGraphics();
		    imgjeju0.setColor(Color.WHITE);
		    imgjeju0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgjeju0fm = imgjeju0.getFontMetrics();
		    int imgjeju0right = ((width - imgjeju0fm.stringWidth(todayjejucnt)-300));
		    
		    imgjeju0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgjeju0.drawString(todayjejucnt, 0 + imgjeju0right, 394);
    		
    		// 부산
		    Graphics2D imgbs0 = covidbuffloc.createGraphics();
		    imgbs0.setColor(Color.WHITE);
		    imgbs0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgbs0fm = imgbs0.getFontMetrics();
		    int imgbs0right = ((width - imgbs0fm.stringWidth(todaybscnt)-75));
		    
		    imgbs0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgbs0.drawString(todaybscnt, 0 + imgbs0right, 106);
    		
    		// 인천
		    Graphics2D imgic0 = covidbuffloc.createGraphics();
		    imgic0.setColor(Color.WHITE);
		    imgic0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgic0fm = imgic0.getFontMetrics();
		    int imgic0right = ((width - imgic0fm.stringWidth(todayiccnt)-75));
		    
		    imgic0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgic0.drawString(todayiccnt, 0 + imgic0right, 142);
    		
    		// 광주
		    Graphics2D imggj0 = covidbuffloc.createGraphics();
		    imggj0.setColor(Color.WHITE);
		    imggj0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imggj0fm = imggj0.getFontMetrics();
		    int imggj0right = ((width - imggj0fm.stringWidth(todaygjcnt)-75));
		    
		    imggj0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imggj0.drawString(todaygjcnt, 0 + imggj0right, 178);
    		
    		// 대구
		    Graphics2D imgdg0 = covidbuffloc.createGraphics();
		    imgdg0.setColor(Color.WHITE);
		    imgdg0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgdg0fm = imgdg0.getFontMetrics();
		    int imgdg0right = ((width - imgdg0fm.stringWidth(todaydgcnt)-75));
		    
		    imgdg0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgdg0.drawString(todaydgcnt, 0 + imgdg0right, 214);
    		
    		// 충북
		    Graphics2D imgcb0 = covidbuffloc.createGraphics();
		    imgcb0.setColor(Color.WHITE);
		    imgcb0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgcb0fm = imgcb0.getFontMetrics();
		    int imgcb0right = ((width - imgcb0fm.stringWidth(todaycbcnt)-75));
		    
		    imgcb0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgcb0.drawString(todaycbcnt, 0 + imgcb0right, 250);
    		
    		// 전북
		    Graphics2D imgjb0 = covidbuffloc.createGraphics();
		    imgjb0.setColor(Color.WHITE);
		    imgjb0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgjb0fm = imgjb0.getFontMetrics();
		    int imgjb0right = ((width - imgjb0fm.stringWidth(todayjbcnt)-75));
		    
		    imgjb0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgjb0.drawString(todayjbcnt, 0 + imgjb0right, 286);
    		
    		// 경북
		    Graphics2D imgkb0 = covidbuffloc.createGraphics();
		    imgkb0.setColor(Color.WHITE);
		    imgkb0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgkb0fm = imgkb0.getFontMetrics();
		    int imgkb0right = ((width - imgkb0fm.stringWidth(todaykbcnt)-75));
		    
		    imgkb0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgkb0.drawString(todaykbcnt, 0 + imgkb0right, 322);
    		
    		// 강원
		    Graphics2D imgkw0 = covidbuffloc.createGraphics();
		    imgkw0.setColor(Color.WHITE);
		    imgkw0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgkw0fm = imgkw0.getFontMetrics();
		    int imgkw0right = ((width - imgkw0fm.stringWidth(todaykwcnt)-75));
		    
		    imgkw0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgkw0.drawString(todaykwcnt, 0 + imgkw0right, 358);
    		
    		// 검역
		    Graphics2D imgflow0 = covidbuffloc.createGraphics();
		    imgflow0.setColor(Color.WHITE);
		    imgflow0.setFont(new Font("서울남산 장체 B", Font.BOLD, 30));
		    FontMetrics imgflow0fm = imgflow0.getFontMetrics();
		    int imgflow0right = ((width - imgflow0fm.stringWidth(todayflowcnt)-75));
		    
		    imgflow0.setRenderingHint(
        			RenderingHints.KEY_TEXT_ANTIALIASING,
        			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		imgflow0.drawString(todayflowcnt, 0 + imgflow0right, 394);
    		
    		File file2 = new File("covidvrc1_old.png");
    		ImageIO.write(covidbuffloc, "png", file2);
        }
    }
}