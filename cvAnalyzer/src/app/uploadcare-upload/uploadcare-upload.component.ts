import { Component } from '@angular/core';
import * as LR from '@uploadcare/blocks';

LR.registerBlocks(LR);

@Component({
  selector: 'app-uploadcare-upload',
  templateUrl: './uploadcare-upload.component.html',
  styleUrls: ['./uploadcare-upload.component.css'],
})
export class UploadcareUploadComponent {
  files: any[] = [];

  handleUploaderEvent(e: Event) {
    const { data: files } = (e as CustomEvent).detail;
    this.files = files;
  }
}
