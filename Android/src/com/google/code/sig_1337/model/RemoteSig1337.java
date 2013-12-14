package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.code.sig_1337.model.handler.RemoteHandler;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IStructure;

/**
 * Remote sig.
 */
public class RemoteSig1337 extends Sig1337Base implements IRemoteSig1337 {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(InputStream is) throws XmlPullParserException,
			IOException, InterruptedException {
		clear();
		try {
			XmlPullParser p = Xml.newPullParser();
			p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			p.setInput(is, null);
			new RemoteHandler<IRemoteSig1337>().handle(this, p);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructure getStructure(double x, double y) {
		// TODO Parler avec le WebService, j'imagine
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IItineraire getItineraire(IBuilding start, IBuilding end) {
		// TODO Auto-generated method stub
		return null;
	}

}
