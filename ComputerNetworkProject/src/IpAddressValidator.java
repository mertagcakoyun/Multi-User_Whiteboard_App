/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author baris
 */ 
import java.util.regex.Pattern;
 
public class IpAddressValidator {
 
    private static final String zeroTo255
            = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
    private static final String avaiblePorts
            = "(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    
 //65535
    private static final String IP_REGEXP
            = zeroTo255 + "\\." + zeroTo255 + "\\."
            + zeroTo255 + "\\." + zeroTo255 ;
 
    private static final Pattern IP_PATTERN
            = Pattern.compile(IP_REGEXP);
 
    // Return true when *address* is IP Address
    boolean isValid(String address) {
        return IP_PATTERN.matcher(address).matches();
    }
}