package net.octacomm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OS에 따라 Ping 명령을 수행 시키고 결과를 리턴한다.
 * 
 * 
 * 성공시:
 * Pinging 192.168.0.135 with 32 bytes of data:
 * Reply from 192.168.0.135: bytes=32 time=2ms TTL=64
 * Ping statistics for 192.168.0.135:
 *     Packets: Sent = 1, Received = 1, Lost = 0 (0% loss),
 *     Approximate round trip times in milli-seconds:
 *         Minimum = 2ms, Maximum = 2ms, Average = 2ms
 * 
 * 실패시:
 * Pinging 192.168.0.131 with 32 bytes of data:
 * Request timed out.
 * Ping statistics for 192.168.0.131:
 *     Packets: Sent = 1, Received = 0, Lost = 1 (100% loss),
 * 
 * @author hslim
 */
public class PingUtil {

    /**
     * Ping 명령행 형식
     */
    private static final String PING_CMDLINE = "cmd.exe /c chcp 437 & ping.exe -n %d -w %d %s";
    /**
     * Ping 결과중 전송한 패킷 및 솟신된 패킷등의 정보를 제공하는 라인 파싱을 위한 정규식
     */
    private static final String PING_PACKET_LINE_REGEXP = ".*= (\\d+),.*= (\\d+), .*= (\\d+) \\(([0-9]+)%.*";
    /**
     * Ping 결과중 통계 정보를 제공하는 라인 파싱을 위한 정규식
     */
    private static final String PING_STATISTICS_LINE_REGEXP = ".*= (\\d+)ms,.*= (\\d+)ms, .*= (\\d+)ms";

    private static Pattern pattern1 = null;
    private static Pattern pattern2 = null;
    
    private static String osName_ = null;
    
    static
    {
    	osName_ = System.getProperty("os.name");
    	pattern1 = Pattern.compile(PING_PACKET_LINE_REGEXP);
        pattern2 = Pattern.compile(PING_STATISTICS_LINE_REGEXP);
    }
    
    /**
     * 생성자
     */
    private PingUtil() {
    }

    /**
     * WINDOW Ping기능
     * 
     * 
     * @param ip
     * @param count
     * @param timeout
     */
    public static PingResult executeWindowPing(String ip, int count, int timeout) 
    {    
        PingResult result = new PingResult();

        if (osName_.indexOf("Windows") == -1) {
            return result;
        }

        String cmdLine = String.format(PING_CMDLINE, count, timeout, ip);
        Process process = null;
        try 
        {
            process = Runtime.getRuntime().exec(cmdLine);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String currentLine = null;

            while ((currentLine = in.readLine()) != null) {
                if (!currentLine.isEmpty()) {
                    parsePingOutput(result, currentLine);
                }
            }
            
            process.destroy();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Ping 표준 출력 문자열 중 걀과인 "Packets:"로 시작하는 문장과 
     * "    Minimum"로 시작하는 문장을 파싱한다.
     * 
     * @param result
     * @param line
     */
    private static void parsePingOutput(PingResult result, String line) 
    {
        // Packets:
        if (line.indexOf("Packets:") != -1) {
            
            Matcher matcher = pattern1.matcher(line);

            if (matcher.matches() && 4 == matcher.groupCount()) {
                result.setLoss(Integer.parseInt(matcher.group(4)));
            }

            return;
        }

        // 통계값
        if (line.indexOf("Minimum") != -1) {
            Matcher matcher = pattern2.matcher(line);

            if (matcher.matches() && 3 == matcher.groupCount()) {
                result.setMin(Integer.parseInt(matcher.group(1)));
                result.setMax(Integer.parseInt(matcher.group(2)));
                result.setAvg(Integer.parseInt(matcher.group(3)));
            }

            return;
        }
    }

    public static boolean ping(String ip) 
    {
        boolean result = false;

        PingResult pingResult = PingUtil.executeWindowPing(ip, 2, 3);
        if (pingResult.getLoss() == 0 && pingResult.getAvg() != -1) 
        {
            result = true;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(ping("192.168.0.101"));
    }
}
