<template>
    <div v-if="!collapsed" id="searchbar" :class="{collapsed: collapsed}">
        <div class="search-field">
            <!-- TODO(Jovan): Add floating label animation -->
            <label class="search-field-label" for="area">Area</label>
            <input class="search-field-input" type="text"
                placeholder="Area" name="area"
                v-model="searchQuery.area" >
        </div>
        <!-- 
        <div class="search-field">
            <label>City</label> <input type="text" placeholder="Which city?"
                name="city">
        </div>
        -->
        <div class="search-field">
            <label>No. of rooms</label>
            <input type="number" placeholder="Number of rooms"
                v-model="searchQuery.noRooms" />
        </div>
        <div class="search-field">
            <label>No. of guests</label>
            <input type="number" placeholder="Number of guests"
                v-model="searchQuery.noGuests"  />
        </div>
        <div class="search-field">
            <label>Pick dates</label>
            <v-date-picker mode="range" v-model="searchQuery.dateRange" :input-props="{placeholder: 'Arrival and departure dates'}"/>
        </div>
        <div class="search-field search-btn" :class="{collapsed: collapsed}">
            <button class="button-primary" @click="search">
                <i class="fa fa-search" aria-hidden="true"></i>
            </button>
        </div>
    </div>

    <!-- NOTE(Jovan): Collapsed, in navbar, searchbar -->
    <div v-else id="searchbar" :class="{collapsed: collapsed}">
        <div class="search-field search-btn" :class="{collapsed: collapsed}">
            <input type="text" placeholder="Search for a home">
            <button type="button" class="button-primary">
                <i class="fa fa-search" aria-hidden="true"></i>
            </button>
        </div>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                allApartments: [],
                searchResults: [],
                searchQuery:
                {
                    area: "",
                    noRooms: "",
                    noGuests: "",
                    dateRange: {},
                    // TODO(Jovan): Search by date
                },
            }
        },
        
        props:
        {
            collapsed:
            {
                type: Boolean,
                default: false,
            },

            user:
            {
                type: Object,
                default: null,
            },
        },

        methods:
        {
            getApartments: function()
            {
                axios.get("rest/get_all_apartments")
                    .then(response => 
                    {
                        response.data.forEach(apartment =>
                        {
                            this.allApartments.push(
                                {
                                    apartment: apartment,
                                    queries:
                                    {
                                        area: apartment.location.address.area,
                                        noRooms: apartment.noRooms,
                                        noGuests: apartment.noGuests,
                                        // TODO(Jovan): Search by date
                                    }
                                }
                            );
                        });
                    });
            },

            search: function()
            {
                let query = this.searchQuery;
                let results = [...this.allApartments];

                console.log("Searching...");

                if(query.area)
                {
                    results = results.filter(a => a.queries.area === query.area);
                }
                if(query.noRooms)
                {
                    results = results.filter(a => a.queries.noRooms == query.noRooms);
                }
                if(query.noGuests)
                {
                    results = results.filter(a => a.queries.noGuests == query.noGuests);
                }
                // TODO(Jovan): Search by date
                this.searchResults = [...results];
                console.log(JSON.stringify(this.searchResults));
                this.$emit("search", results);
            }
        },

        mounted()
        {
            this.getApartments();
        },
    }
</script>

<style scoped>
    #searchbar {
        order: 1;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        min-width: 70vw;
        margin: auto;
        background-color: #fff;
        border-radius: 25px;
    }

    .search-btn {
        border-radius: 50%;
    }

    #searchbar button {
        margin: 0px;
        border-radius: 50%;
        padding: 0px;
        width: 4rem;
        height: 4rem;
    }

    #searchbar input {
        border: none;
        padding: 0px;
        margin: 0px;
        height: 2rem;
    }

    #searchbar input:focus {
        border: none;
    }
</style>