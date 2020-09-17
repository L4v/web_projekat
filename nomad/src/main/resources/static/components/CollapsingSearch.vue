<template>
    <div v-if="!collapsed" id="searchbar" :class="{collapsed: collapsed}">
        <div class="search-field">
            <label class="search-field-label" for="area">Area</label>
            <input class="search-field-input" type="text"
                placeholder="Area" name="area"
                v-model="searchQuery.area" >
        </div>
        <div id="guest-numbers" class="search-field">
            <label>No. of guests</label>
            <div @click="guestsHidden = !guestsHidden">
                <span class="faded-text" v-if="!searchQuery.noGuests">Add guests</span>
                <span v-else>{{searchQuery.noGuests}}</span>
            </div>
            <transition name="fade">
                <div v-if="!guestsHidden" id="guest-numbers-container" @mouseleave="guestsHidden = !guestsHidden">
                    <b>Guests:</b>
                    <div>
                        <button type="button" @click="--searchQuery.noGuests" :disabled="searchQuery.noGuests <= 0">-</button>
                        <span>{{searchQuery.noGuests}}</span>
                        <button type="button" @click="++searchQuery.noGuests">+</button>
                    </div>
                </div>
            </transition>
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
                    area:      "",
                    noGuests:  0,
                    dateRange: {},
                },
                guestsHidden: true,
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
                this.$emit("search", this.searchQuery);
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
        justify-content: space-evenly;
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

    .faded-text,
    ::placeholder
    {
        color: rgb(150, 150, 150);
    }

    .pulse-error
    {
        animation: pulse 2.5s 1;
        border: 2px solid transparent;
        border-radius: 4px;
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

    @keyframes pulse
    {
        0% 
        {
            /* background-color: #fff; */
            border-color: #f00;
        }

        70%
        {
            border-color: #fff;
        }
    }
</style>