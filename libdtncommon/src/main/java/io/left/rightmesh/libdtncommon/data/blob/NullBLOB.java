package io.left.rightmesh.libdtncommon.data.blob;

import io.reactivex.Flowable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * NullBlob is a BaseBLOB of size zero and that contains no data.
 *
 * @author Lucien Loiseau on 04/09/18.
 */
public class NullBLOB implements BLOB {

    @Override
    public Flowable<ByteBuffer> observe() {
        return Flowable.empty();
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public ReadableBLOB getReadableBLOB() {
        return new ReadableBLOB() {
            @Override
            public void read(OutputStream stream) throws IOException {
            }

            @Override
            public void close() {
            }
        };
    }

    @Override
    public WritableBLOB getWritableBLOB() {
        return new WritableBLOB() {
            @Override
            public void clear() {
            }

            @Override
            public int write(InputStream stream, int size) {
                return 0;
            }

            @Override
            public int write(byte b) {
                return 0;
            }

            @Override
            public int write(byte[] a) {
                return 0;
            }

            @Override
            public int write(ByteBuffer buffer) {
                return 0;
            }

            @Override
            public void close() {

            }
        };
    }
}