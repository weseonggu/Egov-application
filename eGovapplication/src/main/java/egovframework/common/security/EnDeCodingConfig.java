package egovframework.common.security;

import org.egovframe.rte.fdl.crypto.EgovARIACryptoService;
import org.egovframe.rte.fdl.crypto.EgovPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class EnDeCodingConfig implements EgovARIACryptoService {
    @Override
    public void setPasswordEncoder(EgovPasswordEncoder egovPasswordEncoder) {

    }

    @Override
    public void setBlockSize(int i) {

    }

    @Override
    public byte[] encrypt(byte[] bytes, String s) {
        return new byte[0];
    }

    @Override
    public BigDecimal encrypt(BigDecimal bigDecimal, String s) {
        return null;
    }

    @Override
    public void encrypt(File file, String s, File file1) throws IOException {

    }

    @Override
    public byte[] decrypt(byte[] bytes, String s) {
        return new byte[0];
    }

    @Override
    public BigDecimal decrypt(BigDecimal bigDecimal, String s) {
        return null;
    }

    @Override
    public void decrypt(File file, String s, File file1) throws IOException {

    }
}
