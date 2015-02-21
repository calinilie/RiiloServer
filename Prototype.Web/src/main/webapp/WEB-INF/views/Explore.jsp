<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<style type="text/css">
html{
	height: 100%;
	margin: 0px;
}
body{
	height:100%;
	margin:0px;
}
#map_canvas{
	height:100%;
}
</style>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
</head>
<body>
<div id="map_canvas"></div>
<script type="text/javascript">
	var map = null;
	var infowindow = null;
	
	var onMap, nearby = "";
	if (window.location.href.indexOf("riilo")>=0){
		nearby = "/posts/nearby/";
		onMap = "/posts/onmap";
	}
	else{
		nearby = "/web/posts/nearby/";
		onMap = "/web/posts/onmap";
	}
	
	function initialize() {
		
		var address = new google.maps.LatLng(40, 0);
		var map_canvas = document.getElementById('map_canvas');
		var map_options = {
		  center: address,
		  zoom: 3, 
		  mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		
		infowindow = new google.maps.InfoWindow({
		      content: 'bla'
		  });
		
		map = new google.maps.Map(map_canvas, map_options);
		
		/* google.maps.event.addListener(map, 'zoom_changed', function() { */
	 	google.maps.event.addListener(map, 'bounds_changed', function() {
			if (map.getZoom()>=10){
				var swlat = map.getBounds().getSouthWest().lat();
				var swlng = map.getBounds().getSouthWest().lng();
				var centerLat = map.getCenter().lat();
				var centerLng = map.getCenter().lng();
				console.log(map.getZoom(), swlat, swlng, centerLat, centerLng);
				
				$.ajax({
					url: nearby+centerLat+"/"+centerLng+"/"+swlat+"/"+swlng+"/",
					success:function (response){
						addMarkers(response);
					}
				});
			}
		  });	
	}
	
	google.maps.event.addDomListener(window, 'load', initialize);
	

	
	var markers = [];
	$(document).ready(function(){
		$.ajax({
			url: onMap,
			success: function (response){
				addMarkers(response);
			}
		});
	});
	
	function addMarkers(postsAsJson){
		$.each(postsAsJson, function(){
			var currentPost = this;
			var exists = false;
			$.each(markers, function(){
				if (currentPost.postId == this.postId){
					exists=true;
				}
			});
			if (!exists){
				var marker = new google.maps.Marker({
					map:map,
					draggable:false,
					/* animation: google.maps.Animation.DROP, */
					position: new google.maps.LatLng(this.latitude, this.longitude),
				  });
				marker.html = this.message;
				marker.postId = this.postId;
				markers.push(marker);
			}
		});
		for (var i = 0; i < markers.length; i++) {
			var marker = markers[i];
			google.maps.event.addListener(marker, 'click', function () {
				// where I have added .html to the marker object.
				infowindow.setContent(this.html);
				infowindow.open(map, this);
				map.setCenter(this.getPosition());
				if (map.getZoom()<10)
					map.setZoom(10);
				
				console.log(this);
			});
		}
	}
</script>
</body>
</html>
