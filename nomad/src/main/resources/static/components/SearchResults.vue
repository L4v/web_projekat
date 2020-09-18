<template>
    <div id="search-main" class="container">
        <div id="search-fields">
            <div id="search-fields-container">
                <floating-label type="text" :inputdata.sync="query.area" placeholder="Area"></floating-label>

                <div>
                    <small class="error">{{errors.noGuests}}</small>
                    <floating-label type="number" :inputdata.sync="query.noGuests" placeholder="No. of guests"></floating-label>
                </div>

                <v-date-picker mode="range" v-model="query.dateRange" :input-props="{placeholder: 'Arrival and departure dates'}"></v-date-picker>
                
                <div>
                    <small class="error">{{errors.fromPrice}}</small>
                    <floating-label type="number" :inputdata.sync="query.fromPrice" placeholder="Price from"></floating-label>
                </div>
                <div>
                    <small class="error">{{errors.toPrice}}</small>
                    <floating-label type="number" :inputdata.sync="query.toPrice" placeholder="Price to"></floating-label>
                </div>
                <div>
                    <small class="error">{{errors.fromRoom}}</small>
                    <floating-label type="number" :inputdata.sync="query.fromRoom" placeholder="Rooms from"></floating-label>
                </div>
                <div>
                    <small class="error">{{errors.toRoom}}</small>
                    <floating-label type="number" :inputdata.sync="query.toRoom" placeholder="Rooms to"></floating-label>
                </div> 
            <button type="button" class="button-primary" @click="search">Search</button>
            </div>
        </div>

        <div id="results">
            <div class="result"
                v-for="apartment in searchResults">
                <div class="result-image">
                    <img :src="'rest/image?filename=' + apartment.images[0]" alt="" style="width: 480px; height: 320px">
                </div>
                <div class="result-text-container">
                    <div class="result-title">
                        <h2>{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</h2>
                    </div>
                    <div id="result-info">
                        <div class="result-text">
                            <b>Host:</b> {{apartment.hostId}}
                        </div>
                        <div class="result-text">
                            <b>No. of guests</b> {{apartment.noGuests}}
                        </div>
                        <div class="result-text">
                            <b>No. of rooms</b> {{apartment.noRooms}}
                        </div>
                        <div class="result-text">
                            <b>Price:</b> {{apartment.price}}
                        </div>
                    </div>
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
                guestsHidden: true,
                query:
                {
                    dateRange: "",
                    fromDate:  "",
                    toDate:    "",
                    area:      "",
                    fromPrice: "",
                    toPrice:   "",
                    fromRoom:  "",
                    toRoom:    "",
                    noGuests:  "",
                },

                searchQuery:
                {
                    fromDate:  "",
                    toDate:    "",
                    area:      "",
                    fromPrice: "",
                    toPrice:   "",
                    fromRoom:  "",
                    toRoom:    "",
                    noGuests:  "",
                },

                errors:
                {
                    fromPrice: "",
                    toPrice:   "",
                    fromRoom:  "",
                    toRoom:    "",
                    noGuests:  "",
                },
            }
        },

        methods:
        {
            validateNoGuests: function()
            {
                if(!this.query.noGuests)
                {
                    this.searchQuery.noGuests = -1;
                    return true;
                }
                if(this.query.noGuests <= 0)
                {
                    this.errors.noGuests = "Should be a positive integer";
                    return false;
                }

                this.searchQuery.noGuests = this.query.noGuests;
                return true;
            },

            validateFromPrice: function()
            {
                if(!this.query.fromPrice)
                {
                    this.searchQuery.fromPrice = -1;
                    return true;
                }
                if(this.query.fromPrice <= 0)
                {
                    this.errors.fromPrice = "Should be a positive integer";
                    return false;
                }

                this.searchQuery.fromPrice = this.query.fromPrice;
                return true;
            },

            validateToPrice: function()
            {
                if(!this.query.toPrice)
                {
                    this.searchQuery.toPrice = -1;
                    return true;
                }
                if(this.query.toPrice <= 0)
                {
                    this.errors.toPrice = "Should be a positive integer";
                    return false;
                }
                this.searchQuery.toPrice = this.query.toPrice;
                return true;
            },

            validateFromRoom: function()
            {
                if(!this.query.fromRoom)
                {
                    this.searchQuery.fromRoom = -1;
                    return true;
                }
                if(this.query.fromRoom <= 0)
                {
                    this.errors.fromRoom = "Should be a positive integer";
                    return false;
                }
                this.searchQuery.fromRoom = this.query.fromRoom;
                return true;
            },

            validateToRoom: function()
            {
                if(!this.query.toRoom)
                {
                    this.searchQuery.toRoom = -1;
                    return true;
                } 
                if(this.query.toRoom <= 0)
                {
                    this.errors.toRoom = "Should be a positive integer";
                    return false;
                }
                this.searchQuery.toRoom = this.query.toRoom;
                return true;
            },

            validateInputs: function()
            {
                this.errors =
                {
                    fromPrice: "",
                    toPrice:   "",
                    fromRoom:  "",
                    toRoom:    "",
                    noGuests:  "",
                };

                let fromPriceValid = this.validateFromPrice();
                let toPriceValid = this.validateToPrice();
                let fromRoomValid = this.validateFromRoom();
                let toRoomValid = this.validateToRoom();
                let noGuestsValid = this.validateNoGuests();

                return fromPriceValid && toPriceValid && fromRoomValid
                    && toRoomValid && noGuestsValid;
            },

            search: function()
            {
                if(!this.validateInputs())
                {
                    return;
                }
                let query = this.searchQuery;
                if(this.query.dateRange)
                {
                    if(this.query.dateRange.start)
                    {
                        query.fromDate = this.query.dateRange.start.getTime();
                    }
                    if(this.query.dateRange.end)
                    {
                        query.toDate = this.query.dateRange.end.getTime();
                    }
                }

                query.fromDate = query.fromDate ? query.fromDate : -1;
                query.toDate = query.toDate ? query.toDate : -1;

                axios.post("rest/search_apartment", query)
                    .then(response =>
                    {
                        this.searchResults = response.data;
                    });
            },

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

<style scoped>
    .vc-w-full
    {
        max-width: 276px;
    }

    #search-main
    {
        display: flex;
        flex-direction: column;
    }

    #search-fields-container
    {
        display: grid;
        grid-template-columns: auto auto auto auto;
        grid-gap: 10px;
    }

    #results
    {
        display: grid;
        grid-template-columns: auto auto;
        grid-gap: 50px;
    }
    .result
    {
        display: flex;
        flex-direction: column;
        border: 1px solid rgb(200, 200, 200);
        border-radius: 8px;
    }

    #result-info
    {
        display: grid;
        grid-template-columns: auto auto;
    }

    .result-text-container:
    {
        padding: 20px;
    }

    .result-title h2
    {
        font-size: 2.2rem;
    }

    .result-text b
    {
        font-weight: 500;
    }

    .faded-text,
    ::placeholder
    {
        color: rgb(150, 150, 150);
    }

    #guest-numbers
    {
        position: relative;
    }

    #guest-numbers-container
    {
        position: absolute;
        background-color: #fff;
        z-index: 3;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;

        min-width: 200px;
        min-height: 50px;
        padding: 16px;

        border-radius: 8px;
    }

    #guest-numbers-container button[disabled]
    {
        color: rgb(200, 200, 200);
    }

</style>