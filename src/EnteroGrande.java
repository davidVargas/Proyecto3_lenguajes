

public final class EnteroGrande extends Number {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient int ival;
	private transient int[] words;
	
	private static final int minFixNum = -100;
	private static final int maxFixNum = 1024;
	private static final int numFixNum = maxFixNum-minFixNum+1;
	private static final EnteroGrande[] smallFixNums = new EnteroGrande[numFixNum];
	
	static
	{
		for (int i = numFixNum;  --i >= 0; )
			smallFixNums[i] = new EnteroGrande(i + minFixNum);
	}
	
	public static final EnteroGrande ZERO = smallFixNums[0 - minFixNum];
	
	public EnteroGrande(int value) {
		this.ival = value;
	}
	
	public EnteroGrande(String value, int radix) {
		EnteroGrande result = valueOf(value, radix);
		this.ival = result.ival;
		this.words = result.words;
	}
	
	public EnteroGrande() {
		// TODO Auto-generated constructor stub
	}

	private EnteroGrande valueOf(String value, int radix) {
		int length = value.length();
		
		if (length <= 15 && radix <= 16)
			return valueOf(Long.parseLong(value, radix));
		
		int i, digit;
		boolean negative;
		byte[] bytes;
		char ch = value.charAt(0);
		
		if (ch == '-') {
			negative = true;
			i = 1;
			bytes = new byte[length -1];
		} else {
			negative = false;
			i = 0;
			bytes = new byte[length];
		}
		
		int byte_length = 0;
		for (; i < length ; i++) {
			ch = value.charAt(i);
			digit = Character.digit(ch, radix);
			if (digit < 0) {
				throw new NumberFormatException();
			}
			bytes[byte_length++] = (byte) digit;
		}
		return valueOf(bytes, byte_length, negative, radix);
	}

	private EnteroGrande valueOf(byte[] digits, int byte_length,
			boolean negative, int radix) {
		
		int char_per_word = MPN.chars_per_word(radix);
		int[] words = new int[byte_length / char_per_word + 1];
		int size = MPN.set_str(words, digits, byte_length, radix);
		if (size == 0)
			return ZERO;
		if(words[size-1] < 0)
			words[size++] = 0;
		if(negative)
			negate(words, words, size);
		return make(words, size);
	}

	private EnteroGrande make(int[] words2, int size) {
		if (words == null)
			return valueOf(size);
		size = EnteroGrande.wordsNeeded(words, size);
		if (size <= 1)
			return size == 0 ? ZERO : valueOf(words[0]);
		EnteroGrande num = new EnteroGrande();
		num.words = words;
		num.ival = size;
		return num;
	}
	
	private static int wordsNeeded(int[] words, int len)
	{
		int i = len;
		if (i > 0)
		{
			int word = words[--i];
			if (word == -1)
			{
				while (i > 0 && (word = words[i - 1]) < 0)
				{
					i--;
					if (word != -1) break;
					}
				}
			else
			{
				while (word == 0 && i > 0 && (word = words[i - 1]) >= 0)  i--;
				}
			}
		return i + 1;
	}

	private static boolean negate(int[] dest, int[] src, int len)
	{
		long carry = 1;
		boolean negative = src[len-1] < 0;
		for (int i = 0;  i < len;  i++)
		{
			carry += ((long) (~src[i]) & 0xffffffffL);
			dest[i] = (int) carry;
			carry >>= 32;
		}
		return (negative && dest[len-1] < 0);
	}

	public static EnteroGrande valueOf(long val)
	{
		if (val >= minFixNum && val <= maxFixNum)
			return smallFixNums[(int) val - minFixNum];
		int i = (int) val;
		if ((long) i == val)
			return new EnteroGrande(i);
		EnteroGrande result = alloc(2);
		result.ival = 2;
		result.words[0] = i;
		result.words[1] = (int)(val >> 32);
		return result;
	}
	
	private static EnteroGrande alloc(int nwords)
	{
		EnteroGrande result = new EnteroGrande();
		if (nwords > 1)
			result.words = new int[nwords];
		return result;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
