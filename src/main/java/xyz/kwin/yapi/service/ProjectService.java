package xyz.kwin.yapi.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.entity.yapi.Response;
import xyz.kwin.yapi.entity.yapi.res.CatMenu;
import xyz.kwin.yapi.entity.yapi.res.ProjectBasicInfo;
import xyz.kwin.yapi.exceptions.YApiException;
import xyz.kwin.yapi.setting.vo.ProjectVO;
import xyz.kwin.yapi.util.YApiClient;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * YApi服务
 *
 * @author kwin
 * @since 2021/12/29 11:25 上午
 */
public class ProjectService {

    private static final Logger log = Logger.getInstance(ProjectService.class);

    public static ProjectService getInstance() {
        return ApplicationManager.getApplication().getService(ProjectService.class);
    }

    /**
     * 获取第一个分类id（一般为公共分类）
     *
     * @param projectToken 项目token
     * @return 分类id
     */
    public Integer getCatid(String projectToken) {
        Response<ProjectBasicInfo> basicInfoResponse = YApiClient.getProjectBasicInfo(projectToken);
        if (!basicInfoResponse.isSuccess()) {
            throw new YApiException(YApiException.YAPI_REQUEST_ERROR);
        }

        Integer projectId = basicInfoResponse.getData().getId();
        Response<List<CatMenu>> catMenuResponse = YApiClient.getCatMenu(projectId, projectToken);
        if (!catMenuResponse.isSuccess()) {
            throw new YApiException(YApiException.YAPI_REQUEST_ERROR);
        }

        if (CollectionUtils.isEmpty(catMenuResponse.getData())) {
            throw new YApiException(CommonConstant.Notice.YAPI_CAT_MENU_EMPTY);
        }

        // 获取第一个分类
        return catMenuResponse.getData().get(0).getId();
    }

    /**
     * 获取ProjectVO列表
     *
     * @param projectTokens 项目token列表
     * @return ProjectVO列表
     */
    public List<ProjectVO> getProjectList(List<String> projectTokens) {
        return projectTokens.stream().map(this::getProject).collect(Collectors.toList());
    }

    /**
     * 获取ProjectVO列表
     * 忽略异常
     *
     * @param projectTokens 项目token列表
     * @return ProjectVO列表
     */
    public List<ProjectVO> getProjectListNoEx(List<String> projectTokens) {
        return projectTokens.stream().map(this::getProject).collect(Collectors.toList());
    }

    /**
     * 获取ProjectVO
     *
     * @param projectToken 项目token
     * @return ProjectVO
     */
    public ProjectVO getProject(String projectToken) {
        ProjectVO projectVO = new ProjectVO(projectToken);
        Response<ProjectBasicInfo> response = YApiClient.getProjectBasicInfo(projectToken);
        if (response.isSuccess()) {
            ProjectBasicInfo basicInfo = response.getData();
            projectVO.setValid(response.isSuccess());
            projectVO.setName(basicInfo.getName());
            projectVO.setProjectId(basicInfo.getId());
        }
        return projectVO;
    }
}
