import { buildBackendUrl } from '@/lib/backend';

type AuthorSummary = {
  id: string;
  username: string;
  walletAddress: string;
};

type CategorySummary = {
  id: string;
  theme: string;
};

export type PostSummary = {
  id: string;
  title: string | null;
  content: string;
  createdAt: string;
  updatedAt: string;
  deleted: boolean;
  author: AuthorSummary;
  category: CategorySummary;
};

const normalizePath = (categoryId?: string) => {
  if (!categoryId) {
    return '/api/posts';
  }

  const params = new URLSearchParams();
  params.set('categoryId', categoryId);
  return `/api/posts?${params.toString()}`;
};

export async function fetchPosts(categoryId?: string): Promise<PostSummary[]> {
  const response = await fetch(buildBackendUrl(normalizePath(categoryId)), {
    cache: 'no-store',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    throw new Error('Failed to fetch posts');
  }

  return (await response.json()) as PostSummary[];
}

export const createSnippet = (content: string, maxLength = 160) => {
  const normalized = content.replace(/\s+/g, ' ').trim();
  if (normalized.length <= maxLength) {
    return normalized;
  }

  return `${normalized.slice(0, maxLength - 1).trim()}…`;
};

export const filterPostsByTheme = (
  posts: PostSummary[],
  theme: string,
) => posts.filter((post) => post.category.theme.toLowerCase() === theme.toLowerCase());
