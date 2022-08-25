import dayjs from 'dayjs/esm';

import { ISqueaks, NewSqueaks } from './squeaks.model';

export const sampleWithRequiredData: ISqueaks = {
  id: 57377,
  content: 'Soft',
};

export const sampleWithPartialData: ISqueaks = {
  id: 76716,
  content: 'Loan De-engineered Steel',
  createdAt: dayjs('2022-08-25T11:49'),
  video: '../fake-data/blob/hipster.png',
  videoContentType: 'unknown',
};

export const sampleWithFullData: ISqueaks = {
  id: 51579,
  content: 'Handcrafted interfaces',
  createdAt: dayjs('2022-08-25T11:18'),
  likes: 74660,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  video: '../fake-data/blob/hipster.png',
  videoContentType: 'unknown',
};

export const sampleWithNewData: NewSqueaks = {
  content: 'panel Dinar virtual',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
