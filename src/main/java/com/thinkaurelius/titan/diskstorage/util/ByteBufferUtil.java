package com.thinkaurelius.titan.diskstorage.util;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
	
	public static final int longSize = 8;

	public static final ByteBuffer getLongByteBuffer(long id) {
		ByteBuffer buffer = ByteBuffer.allocate(longSize);
		buffer.putLong(id);
		buffer.flip();
		return buffer;
	}
	
	public static final ByteBuffer getLongByteBuffer(long[] ids) {
		ByteBuffer buffer = ByteBuffer.allocate(longSize*ids.length);
		for (int i=0;i<ids.length;i++)
			buffer.putLong(ids[i]);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Compares two {@link java.nio.ByteBuffer}s and checks whether the first ByteBuffer is smaller than the second.
	 * 
	 * @param a First ByteBuffer
	 * @param b Second ByteBuffer
	 * @return true if the first ByteBuffer is smaller than the second
	 */
	public static final boolean isSmallerThan(ByteBuffer a, ByteBuffer b) {
		return isSmallerThanWithEqual(a,b,false);
	}
	
	/**
	 * Compares two {@link java.nio.ByteBuffer}s and checks whether the first ByteBuffer is smaller than or equal to the second.
	 * 
	 * @param a First ByteBuffer
	 * @param b Second ByteBuffer
	 * @return true if the first ByteBuffer is smaller than or equal to the second
	 */
	public static final boolean isSmallerOrEqualThan(ByteBuffer a, ByteBuffer b) {
		return isSmallerThanWithEqual(a,b,true);
	}
	
	/**
	 * Compares two {@link java.nio.ByteBuffer}s.
	 * 
	 * If considerEqual is true, it checks whether the first ByteBuffer is smaller than or equal to the second.
	 * If considerEqual is false, it checks whether the first ByteBuffer is smaller than the second.
	 * 
	 * @param a First ByteBuffer
	 * @param b Second ByteBuffer
	 * @param considerEqual Determines comparison mode
	 * @return true if the first ByteBuffer is smaller than (or equal to) the second
	 */
	public static final boolean isSmallerThanWithEqual(ByteBuffer a, ByteBuffer b, boolean considerEqual) {
		if (a == b) {
			return considerEqual;
		}
		a.mark();
		b.mark();
		boolean result = true;
		while (true) {
			if (!a.hasRemaining() && b.hasRemaining()) break;
			else if (a.hasRemaining() && b.hasRemaining()) {
				byte ca = a.get(), cb = b.get();
				if (ca!=cb) {
					if (ca>=0 && cb>=0) {
						if (ca<cb) break;
						else if (ca>cb) { result = false; break; }
					} else if (ca<0 && cb<0) {
						if (ca<cb) break;
						else if (ca>cb) { result = false; break; }
					} else if (ca>=0 && cb<0) break;
					else { result = false; break; }
				}
			} else if (a.hasRemaining() && !b.hasRemaining()) {
				result = false; break;
			} else { //!a.hasRemaining() && !b.hasRemaining()
				result = considerEqual; break; 
			}
		}
		a.reset();
		b.reset();
		return result;
	}
	
	public static final String toBitString(ByteBuffer b, String byteSeparator) {
		StringBuilder s = new StringBuilder();
		while (b.hasRemaining()) {
			byte n = b.get();
			String bn = Integer.toBinaryString(n);
			if (bn.length()>8) bn = bn.substring(bn.length()-8);
			else if (bn.length()<8) {
				while (bn.length()<8) bn = "0" + bn;
			}
			s.append(bn).append(byteSeparator);
		}
		b.rewind();
		return s.toString();
	}
	
}