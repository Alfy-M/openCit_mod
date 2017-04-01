package com.intel.mountwilson.trustagent.commands;

import com.intel.dcsg.cpg.x509.X509Util;
import com.intel.mountwilson.common.CommandUtil;
import com.intel.mountwilson.common.ICommand;
import com.intel.mountwilson.common.TAException;
import com.intel.mountwilson.trustagent.data.TADataContext;
import com.intel.mountwilson.common.ErrorCode;
import com.intel.mtwilson.trustagent.model.IMAList;
import com.intel.mtwilson.trustagent.model.IMAvalue;
import com.intel.mtwilson.trustagent.model.TpmQuoteResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import readingIMAvalues.ImaList;
import readingIMAvalues.ImaValue;

/**
 *
 * @author dsmagadX
 */
public class BuildQuoteXMLCmd implements ICommand {
    Logger log = LoggerFactory.getLogger(getClass().getName());
    private TADataContext context = null;
    private static String defaultPCRnumber = "10";
	private static int template_hash = 1;
	private static int format = 2;
	private static int filedata_hash = 3;
	private static int filename_hint = 4;

    public BuildQuoteXMLCmd(TADataContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        try {
            TpmQuoteResponse tpmQuoteResponse = new TpmQuoteResponse();
            tpmQuoteResponse.timestamp = System.currentTimeMillis();
            tpmQuoteResponse.clientIp = CommandUtil.getHostIpAddress();
            tpmQuoteResponse.errorCode = String.valueOf(context.getErrorCode().getErrorCode());
            tpmQuoteResponse.errorMessage = context.getErrorCode().getMessage();
            tpmQuoteResponse.aik = X509Util.decodePemCertificate(context.getAIKCertificate());
            tpmQuoteResponse.quote = context.getTpmQuote();
            tpmQuoteResponse.eventLog = context.getModules(); //base64-encoded  xml which the caller will interpret
            tpmQuoteResponse.tcbMeasurement = context.getTcbMeasurement();
            tpmQuoteResponse.selectedPcrBanks = context.getSelectedPcrBanks();
            /*********************************************************/
            //We insert here values regarding IMA
            tpmQuoteResponse.ima_values = readValuesFromIma();
            context.setTpmQuoteResponse(tpmQuoteResponse);
        }
        catch(Exception e) {
//            throw new TAException(ErrorCode.ERROR, "Cannot generate tpm quote response", e);
            throw new RuntimeException("Cannot generate tpm quote response", e);
        }
        /*
        String responseXML =
                "<tpm_quote_response> "
                + "<timestamp>" + new Date(System.currentTimeMillis()).toString() + "</timestamp>"
                + "<client_ip>" + StringEscapeUtils.escapeXml(CommandUtil.getHostIpAddress()) + "</client_ip>"
                + "<error_code>" + context.getErrorCode().getErrorCode() + "</error_code>"
                + "<error_message>" + StringEscapeUtils.escapeXml(context.getErrorCode().getMessage()) + "</error_message>"
                + "<aikcert>" + StringEscapeUtils.escapeXml(context.getAIKCertificate()) + "</aikcert>"
                + "<quote>" + new String(Base64.encodeBase64(context.getTpmQuote())) + "</quote>"
                +  "<event_log>" + context.getModules() + "</event_log>" //To add the module information into the response.
                + "</tpm_quote_response>";
        log.debug("Final content that is being sent back to the AS is : " + responseXML);
            context.setResponseXML(responseXML);
*/
    }
    public IMAList readValuesFromIma () {
    	String[] ima_parts = new String[5];
		IMAList list = new IMAList();
		String s;
		try {
			FileReader ima = new FileReader("/sys/kernel/security/ima/ascii_runtime_measurements");
			BufferedReader row = new BufferedReader(ima);
			while (true) {
				s = row.readLine();
				if (s==null)
					break;
				if (!s.startsWith(defaultPCRnumber))
					continue;
				//Inizio delle misure
				
				//value.setPcr(defaultPCRnumber);

				ima_parts=s.split(" ");
				IMAvalue value = new IMAvalue();
				value.setPcr(defaultPCRnumber);
				value.setTemplateHash(ima_parts[template_hash]);
				value.setFormat(ima_parts[format]);
				value.setFiledataHash(ima_parts[filedata_hash]);
				value.setFilenameHint(ima_parts[filename_hint]);
				list.getImaValues().add(value);
			}
		
			row.close();
			ima.close();
			return list;
			
		} catch (IOException e) {
		//Il file non esiste
    	return null;
		}
		
    }
}
