package bg.senpai_main.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class GlobalContentResponseDto<T> {
    private Pageable pageable;
    private long totalPages;
    private long totalElements;
    private long number;
    private long size;
    private boolean first;
    private boolean last;

    public GlobalContentResponseDto(Page<T> page){
        this.pageable = page.getPageable();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}