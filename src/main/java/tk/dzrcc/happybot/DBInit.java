package tk.dzrcc.happybot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.repository.PostRepository;

import javax.annotation.PostConstruct;
import javax.persistence.Column;

/**
 * Created by Maksim on 01.03.2017.
 */
@Component
public class DBInit {
    @Autowired
    PostRepository postRepository;

    @Transactional
    @PostConstruct
    public void Init(){
        //postRepository.save(new Post(123,177,342535));
    }
}
