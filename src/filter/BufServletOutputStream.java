package filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class BufServletOutputStream extends ServletOutputStream {
	// ServletOutputStream是抽象输出流，因此提供一个write()方法的实现
	ByteArrayOutputStream bufferedOut;
	public BufServletOutputStream() {
		bufferedOut = new ByteArrayOutputStream();
	}
	public void write(int i) throws IOException {
		bufferedOut.write(i);
	}
	public byte[] toByteArray() {
		return bufferedOut.toByteArray();
	}
	public void reset() {
		bufferedOut.reset();
	}
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setWriteListener(WriteListener arg0) {
		// TODO Auto-generated method stub
		
	}
}
