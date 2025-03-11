<template>
    <Transition name="fade">
    <div v-show="show">
        <TopBanner />
        <div class="main" v-if="!isNewCategory">
            <div class="single-row top-area">
                <h2>Edit Categories</h2>
                <div class="spacer"></div>
                <p @click="isNewCategory = true"><i class="fas fa-plus"></i></p>
                <p @click="searchButton"><i class="fas fa-search"></i></p>
            </div>
            <div class="search-area" v-show="filter === 'search'">
                <input type="text" v-model="search" placeholder="Search categories" />
                <p @click="search = ''"><i class="fas fa-times"></i></p>
            </div>
            <div>
                <Category v-for="category in categories" :key="category.categoryId" :category="category" @edit="editCategory" @delete="deleteCategory(category)" />
            </div>
        </div>
        <div class="main" v-if="isNewCategory">
            <div>
                <h2>New Category</h2>
                <div class="single-row top-area">   
                    <input type="text" v-model="newCategory" placeholder="Category Name" />
                    <div class="spacer"></div>
                    <p @click="addCategory"><i class="fas fa-plus"></i></p>
                    <p @click="isNewCategory = false"><i class="fas fa-times"></i></p>
                </div>
            </div>
        </div>
    </div>
    </Transition>
</template>

<script>
import TopBanner from '@/components/TopBanner.vue';
import Category from '../components/Category.vue';
import CategoryService from '../services/CategoryService';

export default {
    components: {
        TopBanner, Category
    },
    created() {
        if (this.$store.state.loadedCategories) {
            setTimeout(() => this.show = true, 1);
        } else {
            this.loadingTick()
        }
    },
    data() {
        return {
            filter: "default",
            search: "",
            isNewCategory: false,
            newCategory: "",
            show: false
        }
    },
    computed: {
        categories() {
            if (this.filter === "default" || this.search === "") {
                return this.$store.state.userCategories.slice().sort((a, b) => {
                    if (a.categoryName < b.categoryName) return -1;
                    if (a.categoryName > b.categoryName) return 1;
                    return 0;
                });
            } else if (this.filter === "search" && this.search) {
                return this.$store.state.userCategories.filter(category => category.categoryName.toLowerCase().includes(this.search.toLowerCase())).sort((a, b) => {
                    if (a.categoryName < b.categoryName) return -1;
                    if (a.categoryName > b.categoryName) return 1;
                    return 0;
                });
            }
        }
    },
    methods: {
        editCategory(category) {
            this.$store.commit('EDIT_CATEGORY', category);
        },
        deleteCategory(category) {
            this.$store.commit('DELETE_CATEGORY', category.categoryId);
        },
        searchButton() {
            if (this.filter === "search") {
                this.filter = "default";
            } else {
                this.filter = "search";
            }
        },
        addCategory() {
            let category = {
                categoryName: this.newCategory
            }
            CategoryService.createCategory(category).then(
                (response) => {
                    this.$store.commit('ADD_CATEGORY', response.data);
                    this.newCategory = "";
                    this.isNewCategory = false;
                }
            ).catch(error => {
                console.log(error);
            }
            )
        },
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedCategories) {
                    this.show = true;
                } else {
                    this.loadingTick();
                }
            }, 500);
        }
    }
}
</script>
<style scoped>
    h2 {
        margin: 10px;
    }
    p {
        margin: 0px;
    }
    .main {
        display: flex;
        flex-direction: column;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin-bottom: 10px;
    }
    .top-area {
        display: flex;
        flex-direction: row;
        margin: 5px;
    }
    .spacer {
        flex-grow: 1;
    }
    .search-area {
        margin: 5px;
        display: flex;
        justify-content: center;
    }
    .search-area input {
        padding: 5px;
        border-radius: 5px;
        border: 1px solid var(--border-color);
        width: 75%;
    }
    i {
        margin: 5px;
        border: 1px solid var(--border-color);
        padding: 5px;
        border-radius: 5px;
        background-color: var(--light-2);
    }
</style>