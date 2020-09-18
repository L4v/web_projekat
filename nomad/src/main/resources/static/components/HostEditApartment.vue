<template>
    <div id="edit-main" class="container">
        <select v-model="apartment" required>
            <option value="" disabled>Select apartment</option>
            <option v-for="apt in apartments" :value="apt">{{apt.location.address.street}} {{apt.location.address.streetNumber}} {{apt.location.address.area}}</option>
        </select>
        <transition name="fade">
            <div v-if="apartment" id="apartment-info">
                <div class="edit-container">
                    <image-slider :images="apartment.images"></image-slider>
                </div>
                <div id="location-text" class="edit-container">
                    <floating-label readonly :inputdata.sync="apartment.location.address.street" placeholder="Street" type="text" name="street"></floating-label>
                    <floating-label readonly :inputdata.sync="apartment.location.address.streetNumber" placeholder="Street No." type="text" name="streetNo"></floating-label>
                    <floating-label readonly :inputdata.sync="apartment.location.address.area" placeholder="Area" type="text" name="area"></floating-label>
                    <floating-label readonly :inputdata.sync="apartment.location.address.areaCode" placeholder="Area Code" type="text" name="areaCode"></floating-label>
                </div>
                <div class="edit-container">
                    <map-small :lat="apartment.location.lat" :lon="apartment.location.lon" :zoom="18"></map-small>
                </div>
                <div id="room-info" class="edit-container">
                    <div>
                        <small class="error">{{errors.noGuests}}</small>
                        <floating-label :inputdata.sync="apartment.noGuests" placeholder="Number of guests" type="number"></floating-label>
                    </div>
                    <select v-model="apartment.type">
                        <option value="" disabled>Apartment type</option>
                        <option value="ROOM">Single room</option>
                        <option value="WHOLE">Whole apartment</option>
                    </select>
                    <div>
                        <small v-if="apartment.type === 'WHOLE'" class="error">{{errors.noRooms}}</small>
                        <floating-label v-if="apartment.type === 'WHOLE'" :inputdata.sync="apartment.noRooms" placeholder="Number of rooms" type="number"></floating-label>
                    </div>
                    <div>
                        <small class="error">{{errors.price}}</small>
                        <floating-label :inputdata.sync="apartment.price" placeholder="Price" type="number"></floating-label>
                    </div>
                    <!-- TODO(Jovan): Time!!! -->
                </div>
                <div id="image-upload">
                    <b>Add image</b>
                    <b>{{img_upload_progress}}</b>
                    <input name="image" type="file" accept="image/*" @change="handleImage">
                </div>
                <button type="button" class="button-primary" @click="updateApartment">Update apartment</button>
                <button type="button" @click="$router.go()">Cancel</button>
            </div>
        </transition>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                apartment: "",
                apartments: {},
                img_upload_progress: "",
                errors:
                {
                    noGuests: "",
                    noRooms: "",
                    price: "",
                    date: "",
                },

                newDates: {},

            }
        },

        methods:
        {

            addDate: function()
            {
                this.errors.date = "";
                if(!this.newDates)
                {
                    this.errors.date = "Select date";
                    return;
                }

                this.apartment.availableDates.push({start: new Date(this.newDates.start), end: new Date(this.newDates.end)});
            },

            removeDate: function(index)
            {
                if(index <= 0)
                {
                    console.log("Failed to remove " + index);
                    return;
                }
                this.availableDates.splice(index, 1);
            },

            validateNoRooms: function()
            {
                if(!this.apartment.noRooms  || this.apartment.noRooms <= 0)
                {
                    this.errors.noRooms = "Must be a positive integer";
                    return false;
                }
                return true;
            },

            validateNoGuests: function()
            {
                if(!this.apartment.noGuests || this.apartment.noGuests <= 0)
                {
                    this.errors.noGuests = "Must be a positive integer";
                    return false;
                }
                return true;
            },

            validatePrice: function()
            {
                if(!this.apartment.price)
                {
                    this.errors.price = "Please add a price";
                    return false;
                }

                if(this.apartment.price <= 0)
                {
                    this.errors.price = "Must be a positive integer";
                    return false;
                }

                return true;
            },

            validateInputs: function()
            {
                this.errors =
                {
                    noRooms:  "",
                    noGuests: "",
                    price:    "",
                    errors:   "",

                };

                let noGuestsValid = this.validateNoGuests();
                let noRoomsValid = this.validateNoRooms();
                let priceValid = this.validatePrice();

                return noGuestsValid && noRoomsValid && priceValid;
            },

            updateApartment: function()
            {
                if(!this.validateInputs())
                {
                    return;
                }

                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }

                axios.post("rest/host_update_apartment", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        console.log("Failed update");
                    });
            },

            handleImage: function(e)
            {
                const selectedImage = e.target.files[0];
                this.createBase64Image(selectedImage);
            },

            createBase64Image: function(fileObject)
            {
                const reader = new FileReader();

                reader.onload = (e) =>
                {
                    this.image = e.target.result;
                    this.uploadImage();
                };
                reader.readAsDataURL(fileObject);
            },
            uploadImage: function()
            {
                this.img_upload_progress = "Uploading image...";
                const { image } = this;
                axios.post("rest/host_upload_image", { apartmentId: this.apartment.id, image: image }, {headers: {"Authorization": "Bearer " + localStorage.jwt}})
                    .then(response =>
                    {
                        console.log(response.data);
                        this.img_upload_progress = "Image uploaded";
                    })
                    .catch(response =>
                    {
                        console.log(response.data);
                        this.img_upload_progress = "Image failed uploading";
                    });
            },

            getApartments: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }
                axios.get("rest/host_all_apartments", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        this.apartments = response.data;
                    });
            }
        },
        
        mounted()
        {
            this.getApartments();
        }
    }
</script>

<style scoped>
    .edit-container
    {
        margin-bottom: 16px;
    }

    #room-info
    {
        display: grid;
        grid-template-columns: auto auto;
    }

    #edit-main
    {
        display: flex;
        flex-direction: column;
        justify-content: space-evenly;
    }
    #location-text
    {
        display: grid;
        grid-template-columns: auto auto;
    }
    .fade-enter-active, .fade-leave-active
    {
        transition: opacity .5s;
    }
    .fade-enter, .fade-leave-to
    {
        opacity: 0;
    }

    .error
    {
        color: #f00;
        font-weight: 500;
    }
</style>