package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/8/30.
 */
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {FrequencyPresetServiceImpl.class})})
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class FrequencyPresetServiceImplTest {
    @Resource
    FrequencyPresetMapper frequencyPresetMapper;

    @Test
    @Rollback
    public void getItemCountBySiteId() throws Exception {
        int num = -1;
        num = frequencyPresetMapper.selectItemCountBySiteId(1);
        assertTrue(num > -1);
    }

    @Test
    @Rollback
    public void getPageDataBySiteId() throws Exception {
        int pageCalculate = 0 * 3;
        int siteId = 1;
        int pageSize = 3;
        List<FrequencyPreset> frequencyPresetList = frequencyPresetMapper.selectPageDataBySiteId(siteId, pageCalculate, pageSize);
        for (FrequencyPreset frequencyPreset : frequencyPresetList) {
            assertEquals(Integer.valueOf(1), frequencyPreset.getSiteId());
        }
    }

    @Test
    @Rollback
    public void addFrequencyPreset() throws Exception {
        FrequencyPreset preset = new FrequencyPreset();
        preset.setSiteId(100);
        preset.setUpdateFreq(10);
        preset.setAlertFreq(5);
        frequencyPresetMapper.insert(preset);

        FrequencyPreset preset1 = frequencyPresetMapper.selectByPreset(preset);
        assertEquals(Integer.valueOf(100), preset1.getSiteId());
        assertEquals(Integer.valueOf(10), preset1.getUpdateFreq());
        assertEquals(Integer.valueOf(5), preset1.getAlertFreq());

    }

    @Test
    @Rollback
    public void updateBySiteIdAndId() throws Exception {
        FrequencyPreset preset = new FrequencyPreset();
        preset.setSiteId(100);
        preset.setUpdateFreq(10);
        preset.setAlertFreq(5);
        frequencyPresetMapper.insert(preset);

        FrequencyPreset preset1 = frequencyPresetMapper.selectByPreset(preset);

        FrequencyPreset preset2 = new FrequencyPreset();
        preset2.setSiteId(100);
        preset2.setUpdateFreq(6);
        preset2.setAlertFreq(3);
        preset2.setId(preset1.getId());
        frequencyPresetMapper.updateBySiteIdAndId(preset2);

        FrequencyPreset preset3 = frequencyPresetMapper.selectByPreset(preset2);
        assertEquals(Integer.valueOf(100), preset3.getSiteId());
        assertEquals(Integer.valueOf(6), preset3.getUpdateFreq());
        assertEquals(Integer.valueOf(3), preset3.getAlertFreq());


    }

    @Test
    @Rollback
    public void deleteBySiteIdAndId() throws Exception {
        FrequencyPreset preset = new FrequencyPreset();
        preset.setSiteId(100);
        preset.setUpdateFreq(10);
        preset.setAlertFreq(5);
        frequencyPresetMapper.insert(preset);

        FrequencyPreset preset1 = frequencyPresetMapper.selectByPreset(preset);

        frequencyPresetMapper.deleteBySiteIdAndId(preset1.getSiteId(), preset1.getId());

        FrequencyPreset preset2 = frequencyPresetMapper.selectByPreset(preset1);
        assertEquals(null, preset2);

    }

    @Test
    @Rollback
    public void isPresetFeqIdExist() throws Exception {
        FrequencyPreset preset = new FrequencyPreset();
        preset.setSiteId(100);
        preset.setUpdateFreq(10);
        preset.setAlertFreq(5);
        frequencyPresetMapper.insert(preset);

        FrequencyPreset preset1 = frequencyPresetMapper.selectByPreset(preset);

        FrequencyPreset preset2 = frequencyPresetMapper.selectBySiteIdAndId(preset1.getSiteId(), preset1.getId());

        assertNotNull(preset2);

    }

}