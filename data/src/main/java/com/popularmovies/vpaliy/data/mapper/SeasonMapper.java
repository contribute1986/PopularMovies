package com.popularmovies.vpaliy.data.mapper;

import com.popularmovies.vpaliy.data.configuration.ImageQualityConfiguration;
import com.popularmovies.vpaliy.data.entity.ActorEntity;
import com.popularmovies.vpaliy.data.entity.SeasonEntity;
import com.popularmovies.vpaliy.data.entity.TvShowEpisodeEntity;
import com.popularmovies.vpaliy.data.utils.MapperUtils;
import com.popularmovies.vpaliy.domain.model.ActorCover;
import com.popularmovies.vpaliy.domain.model.SeasonCover;
import com.popularmovies.vpaliy.domain.model.SeasonDetails;
import com.popularmovies.vpaliy.domain.model.TVShowEpisode;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SeasonMapper extends Mapper<SeasonDetails,SeasonEntity>{

    private final Mapper<SeasonCover,SeasonEntity> mapper;
    private final Mapper<ActorCover,ActorEntity> castMapper;
    private final Mapper<TVShowEpisode,TvShowEpisodeEntity> episodeMapper;
    private ImageQualityConfiguration qualityConfiguration;

    @Inject
    public SeasonMapper(Mapper<SeasonCover,SeasonEntity> mapper,
                        Mapper<ActorCover,ActorEntity> castMapper,
                        Mapper<TVShowEpisode,TvShowEpisodeEntity> episodeMapper,
                        ImageQualityConfiguration qualityConfiguration){
        this.mapper=mapper;
        this.castMapper=castMapper;
        this.episodeMapper=episodeMapper;
        this.qualityConfiguration=qualityConfiguration;
    }

    @Override
    public SeasonDetails map(SeasonEntity seasonEntity) {
        SeasonDetails details=new SeasonDetails();
        details.setCast(castMapper.map(seasonEntity.getCast()));
        details.setEpisodeList(episodeMapper.map(seasonEntity.getEpisodes()));
        details.setImages(MapperUtils.convert(seasonEntity.getImages(),qualityConfiguration));
        details.setSeasonCover(mapper.map(seasonEntity));
        details.setSeasonId(seasonEntity.getId());
        details.setVideos(seasonEntity.getVideos());
        return details;
    }

    @Override
    public SeasonEntity reverseMap(SeasonDetails seasonDetails) {
        SeasonEntity seasonEntity=mapper.reverseMap(seasonDetails.getSeasonCover());
        seasonEntity.setEpisodes(episodeMapper.reverseMap(seasonDetails.getEpisodeList()));
        seasonEntity.setVideos(seasonDetails.getVideos());
        seasonEntity.setImages(MapperUtils.convertToBackdrops(seasonDetails.getImages(),qualityConfiguration));
        seasonEntity.setCast(castMapper.reverseMap(seasonDetails.getCast()));
        return seasonEntity;
    }
}
