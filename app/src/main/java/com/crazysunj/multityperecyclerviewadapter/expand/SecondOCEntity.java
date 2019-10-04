package com.crazysunj.multityperecyclerviewadapter.expand;

import androidx.annotation.Nullable;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class SecondOCEntity implements OpenCloseItem {

    public static final int OC_SECOND_TYPE = 6;
    private long id;

    private String title;
    private final int FLAG = 2;

    public SecondOCEntity(String title) {
        this.title = title;
        id = UUID.nameUUIDFromBytes((FLAG + title).getBytes()).hashCode();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getFlag() {
        return FLAG;
    }

    @Override
    public int getItemType() {
        return OC_SECOND_TYPE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SecondOCEntity)) {
            return false;
        }
        if (id != ((SecondOCEntity) obj).id) {
            return false;
        }
        if (title == null) {
            return false;
        }
        return title.equals(((SecondOCEntity) obj).title);
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
