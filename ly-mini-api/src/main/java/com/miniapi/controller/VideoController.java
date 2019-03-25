package com.miniapi.controller;

import com.ly.common.enums.VideoStatusEnum;
import com.ly.common.utils.FetchVideoCover;
import com.ly.common.utils.IMoocJSONResult;
import com.ly.common.utils.MergeVideoMp3;
import com.ly.common.utils.PagedResult;
import com.ly.pojo.Bgm;
import com.ly.pojo.Comments;
import com.ly.pojo.Videos;
import com.ly.service.BgmService;
import com.ly.service.VideoService;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value="视频相关业务的接口", tags= {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController  extends  BasicController{
    @Autowired
    BgmService bgmService ;
    @Autowired
    VideoService videoService ;
    /**
     *
     * @param userId 用户id
     * @param bgmId bgm的id
     * @param videoSeconds 视频长度 .秒
     * @param videoWidth 视频宽度
     * @param videoHeight 视频高度
     * @param desc 视频描述
     * @param file 上传的视频文件
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户上传视频", notes = "用户上传视频的接口")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                   dataType = "String", paramType = "form"),
           @ApiImplicitParam(name = "bgmId", value = "bgmId", required = false,
                   dataType = "String", paramType = "form"),
           @ApiImplicitParam(name = "videoSeconds", value = "视频长度", required = true,
                   dataType = "String", paramType = "form"),
           @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                   dataType = "String", paramType = "form"),
           @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                   dataType = "String", paramType = "form"),
           @ApiImplicitParam(name = "desc", value = "desc", required = false,
                   dataType = "String", paramType = "form")
   })
    @PostMapping(value = "/upload" ,headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId,String bgmId , double videoSeconds ,
    int videoWidth ,int videoHeight ,String desc ,
                                  @ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }


        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video" ;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath="";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                //获取文件名
                String fileNamePrefix = fileName.split("\\.")[0] ;
                //新建封面的文件名
                System.out.println( "fileName: "+ fileName);
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                   finalVideoPath  = FILE_SPACE  + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                //设置图片在数据库中的保存路径
                    coverPathDB = coverPathDB+"/" + fileNamePrefix +".jpg"     ;


                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        //判断bgm是否非空
        //若非空 查询bgm信息 并合并33
        if(StringUtils.isNoneBlank(bgmId)){
            Bgm bgm  = bgmService.queryBgmById(bgmId) ;
            String mp3InputPath = FILE_SPACE + bgm.getPath() ;
            MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3(FFMPEG_EXE);

            String videoOutputName = UUID.randomUUID().toString() + ".mp4" ;
            String videoInputPath = finalVideoPath ;
            //将视频保存在原目录下
            uploadPathDB = "/" + userId + "/video/" + videoOutputName ;
            finalVideoPath=FILE_SPACE + uploadPathDB ;
            mergeVideoMp3.convert(videoInputPath ,mp3InputPath , videoSeconds ,finalVideoPath);

        }
        System.out.println(uploadPathDB);
        System.out.println(finalVideoPath);

        //对视频截图
        FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG_EXE);
        try{
            fetchVideoCover.getCover(finalVideoPath,FILE_SPACE+coverPathDB) ;
        }catch (Exception e){
            e.printStackTrace();
        }



        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
//保存信息到数据库中
        String videoId = videoService.saveVideo(video);


        return IMoocJSONResult.ok(videoId);
    }

    @ApiOperation(value="上传封面", notes="上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoId", value="视频主键id", required=true,
                    dataType="String", paramType="form")
    })
    @PostMapping(value="/uploadCover", headers="content-type=multipart/form-data")
    public IMoocJSONResult uploadCover(String userId,
                                       String videoId,
                                       @ApiParam(value="视频封面", required=true)
                                               MultipartFile file) throws Exception {
        System.out.println("触发保存封面方法 ");
        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空...");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {

                    finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        System.out.println("保存封面的路径"+uploadPathDB);
        videoService.updateVideo(videoId, uploadPathDB);

        return IMoocJSONResult.ok();
    }

    /**
     *
     * @param videos videos 实体类
     * @param isSaveRecord 是否保存
     * @param page 当前页
     * @return
     */
    @PostMapping(value = "/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos videos ,Integer isSaveRecord ,  Integer page){
        PagedResult pagedResult = videoService.getAllVideos(videos  , isSaveRecord ,page, PAGE_SIZE) ;
       return IMoocJSONResult.ok(pagedResult) ;
    }
    /**
     * 热搜关键词查询
     */
    @PostMapping(value = "/hot")
    public IMoocJSONResult hot(){
            return IMoocJSONResult.ok(videoService.getHotWords());
    }

    /**
     *用户喜欢的Controller
     * @return 200
     */
    @PostMapping(value="/userLike")
    public IMoocJSONResult userLike(String userId, String videoId, String videoCreaterId)
            throws Exception {
        videoService.userLikeVideo(userId, videoId, videoCreaterId);
        return IMoocJSONResult.ok();
    }

    /**
     *用户不喜欢的Controller
     * @return 200
     */
    @PostMapping(value = "/userUnLike")
    public IMoocJSONResult userUnLike(String userId ,String videoId , String videoCreaterId){
        System.out.println(userId + " , " + videoCreaterId + " , " + videoId);
        videoService.userUnLikeVideo(userId ,videoId  , videoCreaterId);
        return IMoocJSONResult.ok();
    }
    /**
     * 查询该用户是否喜欢
     */
    @PostMapping(value = "/userLikeOrNot")
    public IMoocJSONResult userLikeOrNot(String userId , String videoId){
       if( videoService.userLikeOrNot(userId,videoId))
       {
           return IMoocJSONResult.ok() ;
       }else
           return IMoocJSONResult.errorMsg("No") ;

    }

    /**
     * 展示我关注的人发布的视频
     */
    @PostMapping("/showMyFollow")
    public IMoocJSONResult showMyFollow(String userId , Integer page) throws  Exception
    {
        if(StringUtils.isBlank(userId))  return IMoocJSONResult.errorMsg("传入的用户名为空");
        if(page == null ) page = 1 ;
        //定义每页展示的数量
        int pageSize = 6 ;

        PagedResult videosList = videoService.queryMyFollowVideos(userId,page,pageSize);
        return IMoocJSONResult.ok(videosList) ;
    }
    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public IMoocJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);

        return IMoocJSONResult.ok(videosList);
    }
    @PostMapping("/getVideoComments")
    public IMoocJSONResult getVideoComments (String videoId ,Integer page , Integer pageSize){
        //判断videoId是否合法
        if(StringUtils.isBlank(videoId)){
            return IMoocJSONResult.ok() ;
        }
        //判断页码是否合法
        if(page == null ) page = 1 ;
        if(pageSize == null ) pageSize = 10 ;
        PagedResult list = videoService.getAllComments(videoId , page , pageSize) ;
        //将封装的list数据返回到前端
        return  IMoocJSONResult.ok(list) ;
    }
    @PostMapping("/saveComment")
    public IMoocJSONResult saveComment(@RequestBody Comments comment,
                                       String fatherCommentId, String toUserId) throws Exception {
//        System.out.println("1: " + comment.toString());
        comment.setFatherCommentId(fatherCommentId);
        comment.setToUserId(toUserId);

        videoService.saveComment(comment);
        return IMoocJSONResult.ok();
    }
    @PostMapping("/delVideoByVideoPath")
    public IMoocJSONResult delVideoByVideoPath(String videoPath){
        //判断videoPath是否合法
        if(StringUtils.isBlank(videoPath)){
            return IMoocJSONResult.errorMsg("传入视频路径不合法") ;
        }
        System.out.println("videoPath:  "+ videoPath);
        boolean delete = videoService.delVideoByVideoPath(videoPath);
        return delete ? IMoocJSONResult.ok() : IMoocJSONResult.errorMsg("删除失败!") ;
    }
//    @PostMapping("saveComment")
//    public IMoocJSONResult saveComments(@RequestBody Comments comments , String fatherCommentId ,String videoId ){
//        //判断fatherCommentId 与 toUserId 是否为空
//        System.out.println("fatherCommentId: " + fatherCommentId + " , videoId: " + videoId );
//        if(StringUtils.isBlank(fatherCommentId) || StringUtils.isBlank(videoId)){
//            System.out.println("传入数据为空");
//            return IMoocJSONResult.errorMsg("传入的被评论视频创作者Id和评论者Id为空");
//        }
//
//        comments.setFatherCommentId(fatherCommentId);
//        String toUserId = videoService.queryUserIdByVideoId(videoId) ;
//        comments.setToUserId(toUserId);
//        System.out.println("videoId: " + videoId);
//        System.out.println("ToUserID: "+toUserId);
//        comments.setVideoId(videoId);
//        videoService.saveComment(comments);
//        return IMoocJSONResult.ok();
//    }
}
