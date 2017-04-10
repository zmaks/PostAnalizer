package tk.dzrcc.happybot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.exceptions.UpdaterException;
import tk.dzrcc.happybot.service.GroupService;
import tk.dzrcc.happybot.service.PostService;
import tk.dzrcc.happybot.vk.VKService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Maksim on 28.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Config.class);
    }

}
