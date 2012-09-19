package es.biohazardwtf.miscfilters;

import java.util.Collection;


//http://greatwebguy.com/programming/java/simple-cross-site-scripting-xss-servlet-filter/
public class CrossSiteScriptingFilter {

}


/*

 * This method tests to see if the string passed in appears to be an explicit 
 * Cross Site Scripting (XSS) attack.  If it appears there is an attack, 
 * an appropriate exception is instantiated (not thrown).
 * <p>
 * Note: This method does not prevent XSS in an application or do filtering of any kind,
 * however it will fire an exception to the intrusion detector if an obvious attack exists.
 * 
 * @see http://www.owasp.org/index.php/AppSensor_DetectionPoints#IE1:_Cross_Site_Scripting_Attempt
 * 
 * @param testValue the string to test for an XSS attack, likely user input from a UI screen.
 * 
 * @return boolean - true if XSS attack found, false if not
 *
public static boolean verifyXSSAttack(String testValue) {
	//AppSensor Checks on Search Value
	Collection<String> attackPatterns = assc.getXSSAttackPatternsList();

	boolean attackFound = false; 
	
	for (String pattern : attackPatterns) {
		if (testValue != null) {
			boolean regexFound = regexFind(testValue, pattern);
			if (regexFound) {
				new AppSensorException("IE1", "AppSensorUser Message IE1", "Attacker is sending a likely XSS attempt (" + testValue + ").");
				attackFound = true;
			}
		}
	}
	
	return attackFound;
}*/