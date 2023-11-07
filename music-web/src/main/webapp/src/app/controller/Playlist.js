"use strict";

/**
 * Playlist controller.
 */
angular.module("music").controller("Playlist", function ($scope, $state, $stateParams, Restangular, Playlist, NamedPlaylist) {
  // Load playlist
  Restangular.one("playlist", $stateParams.id)
    .get()
    .then(function (data) {
      $scope.playlist = data;
    });

  // Play a single track
  $scope.playTrack = function (track) {
    Playlist.removeAndPlay(track);
  };

  // Add a single track to the playlist
  $scope.addTrack = function (track) {
    Playlist.add(track, false);
  };

  // Add all tracks to the playlist in a random order
  $scope.shuffleAllTracks = function () {
    Playlist.addAll(_.shuffle(_.pluck($scope.playlist.tracks, "id")), false);
  };

  // Play all tracks
  $scope.playAllTracks = function () {
    Playlist.removeAndPlayAll(_.pluck($scope.playlist.tracks, "id"));
  };

  // Add all tracks to the playlist
  $scope.addAllTracks = function () {
    Playlist.addAll(_.pluck($scope.playlist.tracks, "id"), false);
  };

  // Like/unlike a track
  $scope.toggleLikeTrack = function (track) {
    Playlist.likeById(track.id, !track.liked);
  };

  // Remove a track
  $scope.removeTrack = function (order) {
    NamedPlaylist.removeTrack($scope.playlist, order).then(function (data) {
      $scope.playlist = data;
    });
  };

  // Delete the playlist
  $scope.remove = function () {
    NamedPlaylist.remove($scope.playlist).then(function () {
      $state.go("main.default");
    });
  };

  // Update UI on track liked
  $scope.$on("track.liked", function (e, trackId, liked) {
    var track = _.findWhere($scope.playlist.tracks, { id: trackId });
    if (track) {
      track.liked = liked;
    }
  });

  // Configuration for track sorting
  $scope.trackSortableOptions = {
    forceHelperSize: true,
    forcePlaceholderSize: true,
    tolerance: "pointer",
    handle: ".handle",
    containment: "parent",
    helper: function (e, ui) {
      ui.children().each(function () {
        $(this).width($(this).width());
      });
      return ui;
    },
    stop: function (e, ui) {
      // Send new positions to server
      $scope.$apply(function () {
        NamedPlaylist.moveTrack($scope.playlist, ui.item.attr("data-order"), ui.item.index());
      });
    },
  };

  //Get recommendations for the tracks in the playlist
  $scope.getRecommendations = function () {
    $scope.recommendationsLastFm = undefined;
    $scope.recommendations = [];
    $scope.tracks_data = $scope.playlist.tracks.map((tr) => {
      return { name: tr.title, artist: tr.artist.name };
    });
    console.log($scope.tracks_data);
    $scope.spotify_seed = {};
    $scope.tracks_data.forEach((tr) => {
      Restangular.one("search/spotify")
        .get({ query: tr.name })
        .then((data) => {
          $scope.spotify_seed.song_id = data.tracks.items[0].id;
          $scope.spotify_seed.artist_id = data.tracks.items[0].artists[0].id;
          Restangular.one("playlist/recommend/spotify")
            .get({ seed_artists: $scope.spotify_seed.artist_id, seed_tracks: $scope.spotify_seed.song_id })
            .then((data) => {
              for (let index = 0; index < 5; index++) {
                $scope.recommendations.push(data.tracks[index]);
              }
              console.log($scope.recommendations);
            });
        });
    });
  };

  $scope.getLastFmRecommendations = function () {
    $scope.recommendationsLastFm = [];
    $scope.recommendations = undefined;
    $scope.tracks_data = $scope.playlist.tracks.map((tr) => {
      return { name: tr.title, artist: tr.artist.name };
    });
    console.log($scope.tracks_data);
    $scope.tracks_data.forEach((tr) => {
      Restangular.one("search/lastfm")
        .get({ query: tr.name })
        .then((data) => {
          console.log(data);
          Restangular.one("playlist/recommend/lastfm")
            .get({ artist: data?.results?.trackmatches?.track[0].artist, track: data?.results?.trackmatches?.track[0].name })
            .then((data) => {
              for (let index = 0; index < 5; index++) {
                if (data.similartracks.track.length != 0) $scope.recommendationsLastFm.push(data?.similartracks?.track[index]);
              }
              console.log(data);
            });
        });
    });
  };
});
