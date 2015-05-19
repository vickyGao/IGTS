package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Cover;
import com.ntu.igts.services.CoverService;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("cover")
public class CoverResource extends BaseResource {

    @Resource
    private CoverService coverService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createCover(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Cover cover = JsonUtil.getPojoFromJsonString(inString, Cover.class);

        if (StringUtil.isEmpty(cover.getCommodityId())) {
            throw new ServiceWarningException("Commodity id cannot is required", MessageKeys.COMMODITY_ID_REQUIERED);
        }
        if (StringUtil.isEmpty(cover.getMainCoverYN())) {
            cover.setMainCoverYN(Constants.N);
        }
        int maxDisplaySequence = coverService.getCurrentMaxDisplaySequenceForCommodity(cover.getCommodityId());
        cover.setDisplaySequence(maxDisplaySequence + 1);
        Cover createdCover = coverService.create(cover);
        return JsonUtil.getJsonStringFromPojo(createdCover);
    }

    @DELETE
    @Path("entity")
    public void delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String coverId) {
        filterSessionContext(token, RoleEnum.USER);
        checkCoverAvailability(coverId);
        coverService.delete(coverId);
    }

    private Cover checkCoverAvailability(String coverId) {
        Cover existingCover = coverService.getById(coverId);
        if (existingCover == null) {
            String[] param = { coverId };
            throw new ServiceWarningException("Cannot find cover for id " + coverId,
                            MessageKeys.COVER_NOT_FOUND_FOR_ID, param);
        } else {
            return existingCover;
        }
    }
}
