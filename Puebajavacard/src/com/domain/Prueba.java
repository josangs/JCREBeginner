/**
 * @author Josan Garrido
 * @since 2015-10-10
 * 
 * Package AID: 'D2 76 00 00 60 50 01'
 * Applet AID: 'D2 76 00 00 60 50 01 00' 
 */

package com.domain;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

public class Prueba extends Applet {
	
	//Constants
	final static byte CLA =(byte) 0x80; //Class of the APDU commands
	final static byte INS_READ = (byte) 0xB0; //Instruction
	
	//Text string which will send back.
	final static byte[] text = {(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte)' ',
		(byte) 'w', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd'};

	private Prueba() {
		register();
	}

	public static void install(byte bArray[], short bOffset, byte bLength)
			throws ISOException {
		new Prueba();
	}
	
	

	public void process(APDU apdu) throws ISOException {
		// TODO Auto-generated method stub
		byte[] commandapdu = apdu.getBuffer();
			
		if (commandapdu[ISO7816.OFFSET_CLA] == CLA) {
			switch (commandapdu[ISO7816.OFFSET_INS]) {
			case INS_READ:
				if ((commandapdu[ISO7816.OFFSET_P1] != 0) || (commandapdu[ISO7816.OFFSET_P2] != 0)) {
					ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
				}//if
				short le = (short)(commandapdu[ISO7816.OFFSET_LC] & 0x00FF); //calculate Le
				short lengthtext = (short)text.length;
				if(le != lengthtext){
					ISOException.throwIt((short)(ISO7816.SW_CORRECT_LENGTH_00 + (short)lengthtext));
				}//if
				apdu.setOutgoing(); //set transmission to outgoing data
				apdu.setOutgoingLength((short)lengthtext); //set the number of bytes to send
				apdu.sendBytesLong(text,(short) 0, lengthtext);
				break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
				break;
			}//switch
		}//if
		else{
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		}//else
	}//process
}
