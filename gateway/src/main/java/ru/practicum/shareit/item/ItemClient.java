package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> createItem(ItemDto itemDto, Integer userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> createItemsComment(CommentDto commentDto, Integer userId, Integer itemId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, Integer userId, Integer itemId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Integer itemId, Integer userId) {
        return get("/" + itemId, userId);
    }

    public void removeItem(Integer userId, Integer itemId) {
        delete("/" + itemId, userId);
    }


    public ResponseEntity<Object> getUsersItems(Integer userId) {
        return get("/", userId);
    }


    public ResponseEntity<Object> getItemByQuery(Long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
       return get("/search", userId, parameters);
    }
}
