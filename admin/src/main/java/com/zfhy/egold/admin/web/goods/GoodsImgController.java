package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.FileUtil;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.goods.entity.GoodsImg;
import com.zfhy.egold.domain.goods.service.GoodsImgService;
import com.zfhy.egold.domain.sys.service.SysfileService;
import com.zfhy.egold.gateway.oss.OssApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zfhy.egold.common.core.result.ResultGenerator.genSuccessResult;


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/goods/goodsImg")
@Slf4j
@Validated
public class GoodsImgController {

    public static String OSS_FILE_URL = "https://egold.oss-cn-shenzhen.aliyuncs.com";

    private String prefix = "goods/goodsImg";
    @Autowired
    private GoodsImgService goodsImgService;

    @Autowired
    private OssApi ossApi;


    @Autowired
    private SysfileService sysfileService;

    @RequiresPermissions("goods:goodsImg:goodsImg")
    @GetMapping()
    String goodsImg() {
        return prefix + "/goodsImg";
    }



    
    @RequiresPermissions("goods:goodsImg:goodsImg")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(GoodsImg.class);
        SqlUtil.genSqlCondition(params, condition);

        List<GoodsImg> goodsImgList = this.goodsImgService.findByCondition(condition);
        PageInfo<GoodsImg> pageInfo = new PageInfo<>(goodsImgList);

        return new PageUtils(goodsImgList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:goodsImg:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("goods:goodsImg:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        GoodsImg goodsImg = goodsImgService.findById(id);
        model.addAttribute("goodsImg", goodsImg);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("goods:goodsImg:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(GoodsImg goodsImg) {

        goodsImg.setCreateDate(new Date());
        goodsImg.setDelFlag("0");
        this.goodsImgService.save(goodsImg);
        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsImg:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(GoodsImg goodsImg) {
        goodsImg.setUpdateDate(new Date());
        this.goodsImgService.update(goodsImg);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsImg:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.goodsImgService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:goodsImg:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] goodsImgIds) {
        if (goodsImgIds == null || goodsImgIds.length == 0) {
            return genSuccessResult();
        }

        String goodsImgIdStr = Stream.of(goodsImgIds).map(Object::toString).collect(Collectors.joining(","));
        this.goodsImgService.deleteByIds(goodsImgIdStr);

        return genSuccessResult();
    }

    @ResponseBody
    @PostMapping("/upload")
    Result<GoodsImg> upload(@RequestParam("file") MultipartFile file, @RequestParam("spuId") Integer spuId, HttpServletRequest request) {

        String originalFilename = file.getOriginalFilename();
        String fileName = "upload/mall/" + FileUtil.RenameToUUID(originalFilename);


        try {
            byte[] fileBytes = file.getBytes();
            ossApi.uploadBytes(fileBytes, fileName);
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new BusException("上传文件失败");
        }

        /*Sysfile sysfile = new Sysfile();
        sysfile.setDelFlag("0");
        sysfile.setCreateDate(new Date());
        sysfile.setType(FileType.fileType(fileName));
        sysfile.setUrl(String.join("/", OSS_FILE_URL, fileName));
        sysfile.setOriginFileName(originalFilename);


        this.sysfileService.save(sysfile);*/

        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setSpuId(spuId);
        goodsImg.setCreateDate(new Date());
        goodsImg.setDelFlag("0");
        goodsImg.setUpdateDate(new Date());
        goodsImg.setImgUrl(String.join("/", OSS_FILE_URL, fileName));

        this.goodsImgService.save(goodsImg);


        return ResultGenerator.genSuccessResult(goodsImg);
    }


}
