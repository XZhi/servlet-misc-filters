package es.biohazardwtf.miscfilters;

public class FilterUtils {
	
	private static final String PAD = " ";
	
	// Original centering method from Sahil Muthoo extracted from
	// http://stackoverflow.com/questions/8154366/how-to-center-a-string-using-string-format
	// Edited to fit project's needs
	public static StringBuilder outputTextCentered( String s , String delimiter , int textlength ) {
    	
    	if( s == null ){
    		s = "null";
    	}else if( s.length() == 0 ){
    		s = PAD;
    	}
        
        StringBuilder sb = new StringBuilder();
        sb.append( delimiter );

        for (int i = 0; i < (textlength - s.length()) / 2; i++) {
            sb.append( PAD );
        }
        
        sb.append(s);
        
        while (sb.length() < textlength-1 ) {
            sb.append(PAD );
        }
        sb.append( delimiter + "\n");
        
        return sb;
    }
    
	
    public static StringBuilder outputTextDelimiter( boolean isHeader , String delimiter , int textLength ){
    	StringBuilder sb = new StringBuilder();
    	if( isHeader ){
    		sb.append("\n");
    	}
    	
    	for( int i=0 ; i< textLength ; i++ ){
    		sb.append( delimiter );
    	}
    	
    	sb.append("\n");
    	
    	return sb;
    }
    
    
    public static StringBuilder outputTextDelimiter( boolean isHeader , String delimiter , String newPadding , int textLength ){
    	StringBuilder sb = new StringBuilder();
    	
    	if( isHeader ){
    		sb.append("\n");
    	}
    	
    	sb.append( delimiter );
    	for( int i=0 ; i< textLength-2 ; i++ ){
    		sb.append( newPadding );
    	}
    	
    	sb.append( delimiter + "\n");    	
    	return sb;
    }
    

}
