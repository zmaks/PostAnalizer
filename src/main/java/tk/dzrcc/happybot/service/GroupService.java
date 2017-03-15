package tk.dzrcc.happybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.dzrcc.happybot.entity.VkGroup;
import tk.dzrcc.happybot.repository.VkGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maksim on 14.03.2017.
 */
@Service
public class GroupService {
    @Autowired
    VkGroupRepository groupRepository;
    public List<Integer> getAllGroupsIds() {
        List<VkGroup> groups = new ArrayList<>();
        groupRepository.findAll().forEach(groups::add);
        List<Integer> groupIds = groups.stream()
                .map(x -> x.getGroupId())
                .collect(Collectors.toList());
        return groupIds;

    }

    public VkGroup getById(Integer ownerId) {
        return groupRepository.findOne(ownerId);
    }
}
