<template>
    <div id="map-container">
    </div>
</template>

<script>
    module.exports =
    {
        mounted()
        {
            var map = new mapboxgl.Map
            ({
                container: 'map-container',
                attributionControl: false,
                center: [-74.5, 40], // starting position [lng, lat]
                zoom: 9 // starting zoom
            });

            let layerStyles =
            {
                "Streets": "streets/vector",
            };
            /*map.addControl(
                new mapboxgl.GeolocateControl
                ({
                    positionOptions:
                    {
                        enableHighAccuracy: true
                    },
                    trackUserLocation: true
                })
            );*/
            map.addControl(
                new locationiqLayerControl({
                    key: locationiq.key,
                    layerStyles: layerStyles
                }), "top-left"
            );
            let geocoder = new MapboxGeocoder(
                {
                    accessToken: mapboxgl.accessToken,
                    mapboxgl: mapboxgl,
                });

            geocoder.on("results", results =>
            {
                console.log("search result: " + results);
                this.$emit("search", results);
            });
            map.addControl(geocoder);
            
        }
    }
</script>

<style scoped>
    #map-container
    {
        width: 100%;
        height: 400px;
    }
</style>