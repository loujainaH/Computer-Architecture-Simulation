
public class test {
public static void main(String[] args) {
//	byte a = 127;
//	byte b = 1;
//	
//	byte res = (byte) (a+b);
//	int res2 = a+b;
//	System.out.println(res2);
////	R2val = (byte) Integer.parseInt(R2,2);  
//	String tmp=  Integer.toString(res2,2);
////	String binary = tmp.substring(tmp.length()-9);
//	if (res2>2) {
//		
//	}
//	
//	
//	System.out.println(Byte.MAX_VALUE);
//	
//	System.out.println((short) unsignedBinaryToDecimal("110"));
//	long x = 12;
//	String x = "11111111111111111111111111110111";
//	System.out.println(x.length());
	String x ="111111111111111111111111";
	System.out.println(x.length());
//	System.out.println(Byte.parseByte("11111111",2));
	System.out.println(Integer.parseUnsignedInt("11111111111111111111111111110111", 2));
}

public static int binaryToInteger(String binary) {
    char[] numbers = binary.toCharArray();
    int result = 0;
    for(int i=numbers.length - 1; i>=0; i--)
        if(numbers[i]=='1')
            result += Math.pow(2, (numbers.length-i - 1));
    return result;
}

static int unsignedBinaryToDecimal(String n)
{
    String num = n;
    int dec_value = 0;

    // Initializing base value to 1,
    // i.e 2^0
    int base = 1;

    int len = num.length();
    for (int i = len - 1; i >= 0; i--) {
        if (num.charAt(i) == '1')
            dec_value += base;
        base = base * 2;
    }

    return dec_value;
}
}
