package com.google.code.sig_1337.model.handler;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.code.sig_1337.model.ISig1337;

/**
 * Handler for the {@code XmlPullParser}.
 */
public interface IHandler<T extends ISig1337> {

	/**
	 * Handle given parser.
	 * 
	 * @param parser
	 *            parser to handle.
	 * @return the result.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	public void handle(T sig, XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException;

}
