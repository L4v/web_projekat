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
            axios.get("rest/get_all_apartments")
                .then(response => 
                {
                    this.allApartments = response.data;

                    axios.get("rest/get_search_results")
                        .then(response =>
                        {
                            this.apartmentIds = response.data;
                            response.data.forEach(id =>
                            {
                                this.searchResults.push(this.allApartments.find(a => a.id === id));
                            });
                        });
                });
        }
    }
</script>