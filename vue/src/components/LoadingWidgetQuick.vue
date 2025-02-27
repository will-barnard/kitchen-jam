vue/src/components/LoadingWidget.vue<template>
    <div>
        <Transition>
            <h2>Loading{{ tickValue }}</h2>
        </Transition>
    </div>
</template>

<script>
export default {
    data() {
        return {
            show: false,
            tickCounter: 0
        }
    },
    methods: {
        tick() {
            setTimeout(() => {
                if (this.tickCounter == 0 || this.tickCounter == 1 || this.tickCounter == 2) {
                    this.tickCounter++;
                } else {
                    this.tickCounter = 0;
                }
                this.tick();
            }, 1000)
        }
    },
    computed: {
        tickValue() {
            let result = "";
            switch (this.tickCounter) {
                case 0: result = "";
                break;
                case 1: result = ".";
                break;
                case 2: result = "..";
                break;
                case 3: result = "...";
                break;
            }
            return result;
        }
    }
}
</script>

<style scoped>
    h2 {
        text-align: center;
    }
    .v-enter-active,
    .v-leave-active {
        transition: opacity .5s ease;
    }

    .v-enter-from,
    .v-leave-to {
        opacity: 0%;
    }
</style>