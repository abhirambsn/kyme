package com.abhirambsn.expenseservice.listener;

import com.abhirambsn.expenseservice.entity.Category;
import com.github.slugify.Slugify;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CategoryEventListener extends AbstractMongoEventListener<Category> {
    final Slugify slg = Slugify.builder().build();
    private String slugify(String name) {
        return slg.slugify(name);
    }

    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<Category> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(slugify(event.getSource().getName()));
            event.getSource().setCreatedAt(LocalDateTime.now());
            event.getSource().setUpdatedAt(LocalDateTime.now());
        }
    }
}
