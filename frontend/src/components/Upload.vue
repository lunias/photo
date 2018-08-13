<template>
<div class="example-full">
  <div class="has-text-right">
  <button type="button" class="button is-warning" @click.prevent="isOption = !isOption">
    <span>Options</span>
    <b-icon icon="settings" size="is-small" />
  </button>
  </div>
  <div v-show="$refs.upload && $refs.upload.dropActive" class="drop-active content">
		<p><span class="title">Drop photos to upload</span></p>
    <p class="has-text-centered"><b-icon icon="cloud-upload" size="is-large" /></p>
  </div>
  <div class="upload" v-show="!isOption">
    <div class="table-container">
      <table class="table is-narrow is-hoverable is-fullwidth is-striped">
        <thead>
          <tr>
            <th>#</th>
            <th>Preview</th>
            <th>Name</th>
            <th>Size</th>
            <th>Speed</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="!files.length">
            <td colspan="7">
              <div class="box has-text-centered content dropbox">
                <p><span class="title">Drop photos anywhere to upload</span></p>
                <p><b-icon icon="cloud-upload" size="is-large" /></p>
              </div>
            </td>
          </tr>
          <tr v-for="(file, index) in files" :key="file.id">
            <td>{{index}}</td>
            <td>
              <img v-if="file.thumb" :src="file.thumb" width="40" height="auto" />
              <span v-else>No Image</span>
            </td>
            <td>
              <div class="filename">
                {{file.name}}
              </div>
              <div class="progress" v-if="file.active || file.progress !== '0.00'">
                <progress
                  :class="{'progress': true, 'is-primary': true, 'is-success': file.success, 'is-danger': file.error, 'progress-bar-animated': file.active}"
                  role="progressbar" :value="file.progress" max="100">
                  {{file.progress}}%
                </progress>
              </div>
            </td>
            <td>{{file.size | formatSize}}</td>
            <td>{{file.speed | formatSize}}/s</td>
            <td v-if="file.error" v-html="file.error"></td>
            <td v-else-if="file.success">success</td>
            <td v-else-if="file.active">active</td>
            <td v-else></td>
            <td>
              <b-dropdown hoverable>
                  <button class="button is-light" slot="trigger">
                    <span>Action</span>
                    <b-icon icon="menu-down" size="is-small" />
                  </button>
                  <b-dropdown-item :class="{'dropdown-item': true, disabled: file.active || file.success || file.error === 'compressing'}" @click="file.active || file.success || file.error === 'compressing' ? false : onEditFileShow(file)">Edit</b-dropdown-item>
                  <b-dropdown-item :class="{'dropdown-item': true, disabled: !file.active}" @click="file.active ? $refs.upload.update(file, {error: 'cancel'}) : false">Cancel</b-dropdown-item>
                  <b-dropdown-item v-if="file.active" @click="$refs.upload.update(file, {active: false})">Abort</b-dropdown-item>
                  <b-dropdown-item v-else-if="file.error && file.error !== 'compressing' && $refs.upload.features.html5" @click="$refs.upload.update(file, {active: true, error: '', progress: '0.00'})">Retry</b-dropdown-item>
                  <b-dropdown-item :class="{'dropdown-item': true, disabled: file.success || file.error === 'compressing'}" href="#" v-else @click="file.success || file.error === 'compressing' ? false : $refs.upload.update(file, {active: true})">Upload</b-dropdown-item>
                  <b-dropdown-item @click="$refs.upload.remove(file)">Remove</b-dropdown-item>
              </b-dropdown>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div>
      <div id="button-box" class="buttons has-addons is-centered">
        <file-upload
          class="button is-primary"
          :post-action="postAction"
          :extensions="extensions"
          :accept="accept"
          :multiple="multiple"
          :directory="directory"
          :size="size || 0"
          :thread="thread < 1 ? 1 : (thread > 5 ? 5 : thread)"
          :drop="drop"
          :drop-directory="dropDirectory"
          :add-index="addIndex"
          v-model="files"
          @input-filter="inputFilter"
          @input-file="inputFile"
          ref="upload">
          Select File(s)
        </file-upload>
        <span type="button" class="button is-info" @click="onAddFolader">
          Select Folder
        </span>
          <span type="button" class="button is-success" :disabled="!files.length || $refs.upload.uploaded" v-if="!$refs.upload || !$refs.upload.active" @click.prevent="$refs.upload.active = true">
            Start Upload
          </span>
          <span type="button" class="button is-dark" v-else @click.prevent="$refs.upload.active = false">
            Stop Upload
          </span>
      </div>
    </div>
      <div id="config-status" class="notification is-warning">
        Drop: <b>{{$refs.upload ? $refs.upload.drop : false}}</b>,
        Active: <b>{{$refs.upload ? $refs.upload.active : false}}</b>,
        Uploaded: <b>{{$refs.upload ? $refs.upload.uploaded : true}}</b>,
        Drop active: <b>{{$refs.upload ? $refs.upload.dropActive : false}}</b>
      </div>
  </div>

  <div class="box" v-show="isOption">
    <div class="form-group">
      <label for="accept">Accept:</label>
      <input type="text" id="accept" class="form-control" v-model="accept">
      <small class="form-text text-muted">Allow upload mime type</small>
    </div>
    <div class="form-group">
      <label for="extensions">Extensions:</label>
      <input type="text" id="extensions" class="form-control" v-model="extensions">
      <small class="form-text text-muted">Allow upload file extension</small>
    </div>
    <div class="form-group">
      <label for="thread">Thread:</label>
      <input type="number" max="5" min="1" id="thread" class="form-control" v-model.number="thread">
      <small class="form-text text-muted">Also upload the number of files at the same time (number of threads)</small>
    </div>
    <div class="form-group">
      <label for="size">Max size:</label>
      <input type="number" min="0" id="size" class="form-control" v-model.number="size">
    </div>
    <div class="form-group">
      <label for="minSize">Min size:</label>
      <input type="number" min="0" id="minSize" class="form-control" v-model.number="minSize">
    </div>
    <div class="form-group">
      <label for="autoCompress">Automatically compress:</label>
      <input type="number" min="0" id="autoCompress" class="form-control" v-model.number="autoCompress">
      <small class="form-text text-muted" v-if="autoCompress > 0">More than {{autoCompress | formatSize}} files are automatically compressed</small>
      <small class="form-text text-muted" v-else>Set up automatic compression</small>
    </div>

    <div class="form-group">
      <div class="form-check">
        <label class="form-check-label">
          <input type="checkbox" id="add-index" class="form-check-input" v-model="addIndex"> Start position to add
        </label>
      </div>
      <small class="form-text text-muted">Add a file list to start the location to add</small>
    </div>

    <div class="form-group">
      <div class="form-check">
        <label class="form-check-label">
          <input type="checkbox" id="drop" class="form-check-input" v-model="drop"> Drop
        </label>
      </div>
      <small class="form-text text-muted">Drag and drop upload</small>
    </div>
    <div class="form-group">
      <div class="form-check">
        <label class="form-check-label">
          <input type="checkbox" id="drop-directory" class="form-check-input" v-model="dropDirectory"> Drop directory
        </label>
      </div>
      <small class="form-text text-muted">Not checked, filter the dragged folder</small>
    </div>
    <div class="form-group">
      <div class="form-check">
        <label class="form-check-label">
          <input type="checkbox" id="upload-auto" class="form-check-input" v-model="uploadAuto"> Auto start
        </label>
      </div>
      <small class="form-text text-muted">Automatically activate upload</small>
    </div>
    <div class="form-group">
      <button type="button" class="button is-primary  is-fullwidth" @click.prevent="isOption = !isOption">Confirm</button>
    </div>
  </div>

  <div :class="{'modal-backdrop': true, 'fade': true, show: addData.show}"></div>
  <div :class="{modal: true, fade: true, show: addData.show}" id="modal-add-data" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Add data</h5>
          <button type="button" class="close"  @click.prevent="addData.show = false">
            <span>&times;</span>
          </button>
        </div>
        <form @submit.prevent="onAddData">
          <div class="modal-body">
            <div class="form-group">
              <label for="name">Name:</label>
              <input type="text" class="form-control" required id="name"  placeholder="Please enter a file name" v-model="addData.name">
              <small class="form-text text-muted">Such as <code>filename.txt</code></small>
            </div>
            <div class="form-group">
              <label for="type">Type:</label>
              <input type="text" class="form-control" required id="type"  placeholder="Please enter the MIME type" v-model="addData.type">
              <small class="form-text text-muted">Such as <code>text/plain</code></small>
            </div>
            <div class="form-group">
              <label for="content">Content:</label>
              <textarea class="form-control" required id="content" rows="3" placeholder="Please enter the file contents" v-model="addData.content"></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="button btn-secondary" @click.prevent="addData.show = false">Close</button>
            <button type="submit" class="button btn-primary">Save</button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <div :class="{'modal-backdrop': true, 'fade': true, show: editFile.show}"></div>
  <div :class="{modal: true, fade: true, show: editFile.show}" id="modal-edit-file" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit file</h5>
          <button type="button" class="close"  @click.prevent="editFile.show = false">
            <span>&times;</span>
          </button>
        </div>
        <form @submit.prevent="onEditorFile">
          <div class="modal-body">
            <div class="form-group">
              <label for="name">Name:</label>
              <input type="text" class="form-control" required id="name"  placeholder="Please enter a file name" v-model="editFile.name">
            </div>
            <div class="form-group" v-if="editFile.show && editFile.blob && editFile.type && editFile.type.substr(0, 6) === 'image/'">
              <label>Image: </label>
              <div class="edit-image">
                <img :src="editFile.blob" ref="editImage" />
              </div>

              <div class="edit-image-tool">
                <div class="btn-group" role="group">
                  <button type="button" class="button btn-primary" @click="editFile.cropper.rotate(-90)" title="cropper.rotate(-90)"><i class="fa fa-undo" aria-hidden="true"></i></button>
                  <button type="button" class="button btn-primary" @click="editFile.cropper.rotate(90)"  title="cropper.rotate(90)"><i class="fa fa-repeat" aria-hidden="true"></i></button>
                </div>
                <div class="btn-group" role="group">
                  <button type="button" class="button btn-primary" @click="editFile.cropper.crop()" title="cropper.crop()"><i class="fa fa-check" aria-hidden="true"></i></button>
                  <button type="button" class="button btn-primary" @click="editFile.cropper.clear()" title="cropper.clear()"><i class="fa fa-remove" aria-hidden="true"></i></button>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="button btn-secondary" @click.prevent="editFile.show = false">Close</button>
            <button type="submit" class="button btn-primary">Save</button>
          </div>
        </form>
      </div>
    </div>
  </div>

</div>
</template>
<style>
.example-full .drop-active {
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
    position: fixed;
    z-index: 9999;
    opacity: .6;
    text-align: center;
    background: #000;
}

.example-full .drop-active span {
    margin: -.5em 0 0;
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
    font-size: 40px;
    color: #fff;
    padding: 0;
}

#config-status {
    margin: .5em;
    padding: 10px;
}

#button-box {
    margin-top: -1em;
    margin-bottom: 1em;
}
.table-container {
    overflow-x: visible;
    overflow-y: auto;
    height: 350px;
}
.dropbox {
    height: 300px;
}
.dropdown-menu {
    min-width: 6em;
}
</style>

<script>
import Cropper from 'cropperjs'
import ImageCompressor from '@xkeshi/image-compressor'
import FileUpload from 'vue-upload-component'
export default {
    components: {
        FileUpload
    },
    
    data() {
        return {
            files: [],
            accept: 'image/png,image/gif,image/jpeg,image/webp',
            extensions: 'gif,jpg,jpeg,png,webp',
            // extensions: ['gif', 'jpg', 'jpeg','png', 'webp'],
            // extensions: /\.(gif|jpe?g|png|webp)$/i,
            minSize: 1024,
            size: 1024 * 1024 * 10,
            multiple: true,
            directory: false,
            drop: true,
            dropDirectory: true,
            addIndex: false,
            thread: 3,
            name: 'file',
            postAction: '/api/photos/upload',
            autoCompress: 1024 * 1024,
            uploadAuto: false,
            isOption: false,            
            addData: {
                show: false,
                name: '',
                type: '',
                content: '',
            },                        
            editFile: {
                show: false,
                name: '',
            }
        }
    },
    
    watch: {
        'editFile.show'(newValue, oldValue) {
            // 关闭了 自动删除 error
            if (!newValue && oldValue) {
                this.$refs.upload.update(this.editFile.id, { error: this.editFile.error || '' })
            }
            
            if (newValue) {
                this.$nextTick(function () {
                    if (!this.$refs.editImage) {
                        return
                    }
                    let cropper = new Cropper(this.$refs.editImage, {
                        autoCrop: false,
                    })
                    this.editFile = {
                        ...this.editFile,
                        cropper
                    }
                })
            }
        },
        
        'addData.show'(show) {
            if (show) {
                this.addData.name = ''
                this.addData.type = ''
                this.addData.content = ''
            }
        },
    },
    beforeMount: function() {
        this.updateBatchId()
    },
    methods: {
        updateBatchId() {
            var d = new Date().getTime();
            if (typeof performance !== 'undefined' && typeof performance.now === 'function'){
                d += performance.now(); //use high-precision timer if available
            }
            this.batchId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
        },
        inputFilter(newFile, oldFile, prevent) {
            if (newFile && !oldFile) {
                // Before adding a file
                // 添加文件前
                
                // Filter system files or hide files
                // 过滤系统文件 和隐藏文件
                if (/(\/|^)(Thumbs\.db|desktop\.ini|\..+)$/.test(newFile.name)) {
                    return prevent()
                }
                
                // Filter php html js file
                // 过滤 php html js 文件
                if (/\.(php5?|html?|jsx?)$/i.test(newFile.name)) {
                    return prevent()
                }
                
                // Automatic compression
                // 自动压缩
                if (newFile.file && newFile.type.substr(0, 6) === 'image/' && this.autoCompress > 0 && this.autoCompress < newFile.size) {
                    newFile.error = 'compressing &hellip;'
                    const imageCompressor = new ImageCompressor(null, {
                        convertSize: Infinity,
                        maxWidth: 512,
                        maxHeight: 512,
                    })
                    imageCompressor.compress(newFile.file)
                        .then((file) => {
                            this.$refs.upload.update(newFile, { error: '', file, size: file.size, type: file.type })
                        })
            .catch((err) => {
              this.$refs.upload.update(newFile, { error: err.message || 'compress' })
            })
        }
      }


      if (newFile && (!oldFile || newFile.file !== oldFile.file)) {

        // Create a blob field
        // 创建 blob 字段
        newFile.blob = ''
        let URL = window.URL || window.webkitURL
        if (URL && URL.createObjectURL) {
          newFile.blob = URL.createObjectURL(newFile.file)
        }

        // Thumbnails
        // 缩略图
        newFile.thumb = ''
        if (newFile.blob && newFile.type.substr(0, 6) === 'image/') {
          newFile.thumb = newFile.blob
        }

          newFile.data = {'batchId': this.batchId}
      }
    },

    // add, update, remove File Event
    inputFile(newFile, oldFile) {
      if (newFile && oldFile) {
        // update

        if (newFile.active && !oldFile.active) {
          // beforeSend

          // min size
          if (newFile.size >= 0 && this.minSize > 0 && newFile.size < this.minSize) {
            this.$refs.upload.update(newFile, { error: 'size' })
          }
        }

        if (newFile.progress !== oldFile.progress) {
          // progress
        }

        if (newFile.error && !oldFile.error) {
          // error
        }

        if (newFile.success && !oldFile.success) {
          // success
        }
      }


      if (!newFile && oldFile) {
        // remove
        if (oldFile.success && oldFile.response.id) {
          // $.ajax({
          //   type: 'DELETE',
          //   url: '/upload/delete?id=' + oldFile.response.id,
          // })
        }
      }


      // Automatically activate upload
      if (Boolean(newFile) !== Boolean(oldFile) || oldFile.error !== newFile.error) {
        if (this.uploadAuto && !this.$refs.upload.active) {
          this.$refs.upload.active = true
        }
      }
    },


    alert(message) {
      alert(message)
    },


    onEditFileShow(file) {
      this.editFile = { ...file, show: true }
      this.$refs.upload.update(file, { error: 'edit' })
    },

    onEditorFile() {
      if (!this.$refs.upload.features.html5) {
        this.alert('Your browser does not support')
        this.editFile.show = false
        return
      }

      let data = {
        name: this.editFile.name,
      }
      if (this.editFile.cropper) {
        let binStr = atob(this.editFile.cropper.getCroppedCanvas().toDataURL(this.editFile.type).split(',')[1])
        let arr = new Uint8Array(binStr.length)
        for (let i = 0; i < binStr.length; i++) {
          arr[i] = binStr.charCodeAt(i)
        }
        data.file = new File([arr], data.name, { type: this.editFile.type })
        data.size = data.file.size
      }
      this.$refs.upload.update(this.editFile.id, data)
      this.editFile.error = ''
      this.editFile.show = false
    },

    // add folader
    onAddFolader() {
      if (!this.$refs.upload.features.directory) {
        this.alert('Your browser does not support')
        return
      }

      let input = this.$refs.upload.$el.querySelector('input')
      input.directory = true
      input.webkitdirectory = true
      this.directory = true

      input.onclick = null
      input.click()
      input.onclick = (e) => {
        this.directory = false
        input.directory = false
        input.webkitdirectory = false
      }
    },

    onAddData() {
      this.addData.show = false
      if (!this.$refs.upload.features.html5) {
        this.alert('Your browser does not support')
        return
      }

      let file = new window.File([this.addData.content], this.addData.name, {
        type: this.addData.type,
      })
      this.$refs.upload.add(file)
    }
  }
}
</script>
