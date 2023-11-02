import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadcareUploadComponent } from './uploadcare-upload.component';

describe('UploadcareUploadComponent', () => {
  let component: UploadcareUploadComponent;
  let fixture: ComponentFixture<UploadcareUploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadcareUploadComponent]
    });
    fixture = TestBed.createComponent(UploadcareUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
