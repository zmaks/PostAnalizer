package tk.dzrcc.happybot.service;

import com.vk.api.sdk.objects.groups.GroupFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.Utils;
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
        return groups.stream()
                .filter(x -> x.getActive())
                .map(x -> x.getGroupId())
                .collect(Collectors.toList());

    }

    @Transactional
    public VkGroup getById(Integer ownerId) {
        return groupRepository.findOne(ownerId);
    }

    @Transactional
    public VkGroup updateGroup(VkGroup group, Integer likes, Integer reposts, Integer views) {
        Integer count = group.getCount();
        if (count == null) {
            group.setAvgLikes(likes);
            group.setAvgShares(reposts);
            group.setAvgViews(views);
            group.setCount(1);
        } else {
            group.setAvgLikes(Utils.calculateNewAvgValue(group.getAvgLikes(), likes, count));
            group.setAvgShares(Utils.calculateNewAvgValue(group.getAvgShares(), reposts, count));
            group.setAvgViews(Utils.calculateNewAvgValue(group.getAvgViews(), views, count));
            group.setCount(count+1);
        }

        return groupRepository.save(group);
    }


    public void saveGroups(List<GroupFull> groups) {
        for (GroupFull group : groups) {
            Integer groupId = Integer.parseInt(group.getId())*-1;
            VkGroup vkGroup = groupRepository.findOne(groupId);
            if (vkGroup == null){
                vkGroup = new VkGroup();
                vkGroup.setGroupId(groupId);
            }
            vkGroup.setName(group.getName());
            vkGroup.setMembersCount(group.getMembersCount());
            groupRepository.save(vkGroup);
        }
    }
}
