<template>
    <div id="search-main">
        <div id="results">
            <div class="result"
                v-for="apartment in searchResults">
                <div class="result-text">
                    {{apartment.location.address.street}} {{apartment.location.address.streetNumber}}{{apartment.location.address.area}} 
                </div>
                <div class="result-text">
                    <b>Host:</b> {{apartment.hostId}}
                </div>
                <div class="result-text">
                    <b>No. of guests</b> {{apartment.noGuests}}
                </div>
                <div class="result-text">
                    <b>No. of rooms</b> {{apartment.noRooms}}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                searchResults: [],
                allApartments: [],
                apartmentIds: [],
            }
        },

        methods:
        {

        },
        mounted()
        {
            let query = this.$route.query;
            axios.post("rest/search_apartment", query)
                .then(response =>
                {
                    this.searchResults = response.data;
                });
        }
    }
</script>