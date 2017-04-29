/*
 * XMLRSS - A Java Crypto Provider for Redactable Signatures and their
 * XML Signature encoding.
 *
 * Copyright (c) 2017 Wolfgang Popp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.unipassau.wolfgangpopp.xmlrss.wpprovider.grss.xml;

import de.unipassau.wolfgangpopp.xmlrss.wpprovider.grss.GLRSSSignatureOutput;
import de.unipassau.wolfgangpopp.xmlrss.wpprovider.utils.ByteArray;
import de.unipassau.wolfgangpopp.xmlrss.wpprovider.xml.Proof;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Wolfgang Popp
 */
@XmlRootElement(name = "Proof")
public class GLProof implements Proof {

    @XmlElement(name = "SimpleProof")
    private String gsProof;

    @XmlElement(name = "RandomValue")
    private String randomValue;

    @XmlElement(name = "AccumulatorValue")
    private String accumulatorValue;

    @XmlElementWrapper(name = "Witnesses")
    @XmlElement(name = "Witness")
    private final List<String> witnesses = new ArrayList<>();

    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    private GLProof() {

    }

    public GLProof(GLRSSSignatureOutput.GLRSSSignedPart signedPart) {
        this.randomValue = encoder.encodeToString(signedPart.getRandomValue());
        this.accumulatorValue = encoder.encodeToString(signedPart.getAccumulatorValue());
        this.gsProof = encoder.encodeToString(signedPart.getGsProof());
        for (ByteArray byteArray : signedPart.getWitnesses()) {
            witnesses.add(encoder.encodeToString(byteArray.getArray()));
        }
    }

    public byte[] getGsProof() {
        return decoder.decode(gsProof);
    }

    public byte[] getRandomValue() {
        return decoder.decode(randomValue);
    }

    public byte[] getAccumulatorValue() {
        return decoder.decode(accumulatorValue);
    }

    public List<ByteArray> getWitnesses() {
        List<ByteArray> decoded = new ArrayList<>(witnesses.size());
        for (String witness : witnesses) {
            decoded.add(new ByteArray(decoder.decode(witness)));
        }
        return decoded;
    }
}
